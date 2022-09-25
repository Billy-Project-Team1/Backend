package com.sparta.billy.socket.repository;

import com.sparta.billy.socket.dto.ChatMessageDto;
import com.sparta.billy.socket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE"; // 채팅룸에 메세지들을 저장

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate; // redisTemplate 사용
    private final StringRedisTemplate stringRedisTemplate; // StringRedisTemplate 사용
    private HashOperations<String, String, String> hashOpsEnterInfo; // Redis 의 Hashes 사용
    private HashOperations<String, String, List<ChatMessageDto>> opsHashChatMessage; // Redis 의 Hashes 사용
    private ValueOperations<String, String> valueOps; // Redis 의 String 구조 사용

    //초기화
    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
        hashOpsEnterInfo = redisTemplate.opsForHash();
        valueOps = stringRedisTemplate.opsForValue();
    }

    //redis 에 메세지 저장하기
    @Transactional
    public ChatMessageDto save(ChatMessageDto chatMessageDto) {
        //chatMessageDto 를 redis 에 저장하기 위하여 직렬화 한다.
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class)); //setValueSerializer() 메소드를 사용하여 직렬화
        String roomId = chatMessageDto.getRoomId();
        //redis에 저장되어있는 리스트를 가져와, 새로 받아온 chatmessageDto를 더하여 다시 저장한다.
        List<ChatMessageDto> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, roomId);
        //가져온 List가 null일때 새로운 리스트를 만든다 == 처음에 메세지를 저장할경우 리스트가 없기때문에.
        if (chatMessageList == null) {
            chatMessageList = new ArrayList<>();
        }
        chatMessageList.add(chatMessageDto);
        //redis 의 hashes 자료구조
        //key : CHAT_MESSAGE , filed : roomId, value : chatMessageList
        opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageList);
        redisTemplate.expire(CHAT_MESSAGE,24, TimeUnit.HOURS);
        return chatMessageDto;
    }

    //채팅 리스트 가져오기
    @Transactional
    public List<ChatMessageDto> findAllMessage(String roomId) {
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        //chatMessage 리스트를 불러올때, 리스트의 사이즈가 0보다 크면 redis 정보를 가져온다
        //redis 에서 가져온 리스트의 사이즈가  0보다 크다 == 저장된 정보가 있다.
        if (opsHashChatMessage.size(CHAT_MESSAGE) > 0) {
            return (opsHashChatMessage.get(CHAT_MESSAGE, roomId));
        } else { // redis 에서 가져온 메세지 리스트의 사이즈가 0보다 작다 == redis에 정보가 없다.
            List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByRoomId(roomId);

            for (ChatMessage chatMessage : chatMessages) {
                LocalDateTime createdAt = chatMessage.getCreatedAt();
                String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));
                ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage,createdAtString);
                chatMessageDtoList.add(chatMessageDto);
            }
            //redis에 정보가 없으니, 다음부터 조회할때는 redis를 사용하기 위하여 넣어준다.
            opsHashChatMessage.put(CHAT_MESSAGE, roomId, chatMessageDtoList);
            return chatMessageDtoList;
        }
    }


}