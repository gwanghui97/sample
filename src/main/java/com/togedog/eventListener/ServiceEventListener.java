package com.togedog.eventListener;

import com.togedog.matching.service.MatchingService;
import com.togedog.matchingStandBy.entity.MatchingStandBy;
import com.togedog.matchingStandBy.service.MatchingStandByService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.togedog.eventListener.EventCaseEnum.EventCase.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceEventListener {
    private final MatchingService matchingService;
    private final MatchingStandByService matchingStandByService;

    @EventListener
    public void handleMyCustomEvent(CustomEvent event) {
        switch (event.getMethodName()) {
            case DELETE_RELATED_MATCHING_STAND_BY_DATA:
                log.debug("Event : DELETE_RELATED_MATCHING_STAND_BY_DATA");
                matchingStandByService.extractedDataAndChangeStatusToFail((Long) event.getResource(),
                        MatchingStandBy.Status.STATUS_WAIT);
                break;
            case SUCCESS_RELATED_MATCHING_DATA:
                log.debug("Event : SUCCESS_RELATED_MATCHING_DATA");
                matchingService.updateMatchForCustomEvent(event.getResources().get(0),event.getResources().get(1));
                break;
        }
    }
}
