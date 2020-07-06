package shopide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import shopide.config.kafka.KafkaProcessor;

import java.util.List;

@Service
public class MypageViewHandler {

    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_ (@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                // view 객체 생성
                Mypage mypage = new Mypage();
                // view 객체에 이벤트의 Value 를 set 함
                mypage.setOrderId(ordered.getId());
                mypage.setProdId(Long.parseLong(ordered.getProductId()));
                mypage.setQty(ordered.getQty());
                //mypage.set(ordered.get());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenShipped_then_UPDATE_(@Payload Shipped shipped) {
        try {
            if (shipped.isMe()) {
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByOrderId(shipped.getOrderId());
                for(Mypage mypage : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setDeliveryId(shipped.getId());
                    mypage.setStatus(shipped.getStatus());
                    mypage.setOrderId(shipped.getOrderId());
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (deliveryCanceled.isMe()) {
                // view 객체 조회
                List<Mypage> mypageList = mypageRepository.findByOrderId(Long.parseLong(deliveryCanceled.getOrderId()));
                for(Mypage mypage : mypageList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setStatus(deliveryCanceled.getStatus());
                    mypage.setOrderId(Long.parseLong(deliveryCanceled.getOrderId()));
                    // view 레파지 토리에 save
                    mypageRepository.save(mypage);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @StreamListener(KafkaProcessor.INPUT)
//    public void when_then_DELETE_(@Payload  ) {
//        try {
//            if (.isMe()) {
//                // view 레파지 토리에 삭제 쿼리
//                mypageRepository.deleteBy(.get());
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}