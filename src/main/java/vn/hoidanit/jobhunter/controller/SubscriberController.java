package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<Subscriber>> createSubscriber(@Valid @RequestBody Subscriber subscriber) {
        // check trung email, skills truyen len
        Subscriber createdSubscriber = this.subscriberService.create(subscriber);
        return ResponseFactory.success(createdSubscriber, "Create subscriber successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<Subscriber>> updateSubscriber(@RequestBody Subscriber subscriber) {
        // check trung email, skills truyen len
        Subscriber createdSubscriber = this.subscriberService.update(subscriber);
        return ResponseFactory.success(createdSubscriber, "Update subscriber successfully", HttpStatus.CREATED);
    }

    @PostMapping("/skills")
    public ResponseEntity<RestResponse<Subscriber>> getSubscriberSkills() {
        // check trung email, skills truyen len
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        Subscriber subscriber = this.subscriberService.findByEmail(email);
        return ResponseFactory.success(subscriber, "Get subscriber's skills successfully");
    }
}
