package vn.hoidanit.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService,
            SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    // @GetMapping
    // public String sendSimpleEmail() {
    // this.emailService.sendEmailSync(
    // "tantruongspring@gmail.com",
    // "Test send email",
    // "<h1><b style=\"font-size: 22px; color: red;\";>Hello from spring
    // email</b></h1>",
    // false,
    // true);
    // return "OK";
    // }

    // @GetMapping
    // public String sendSimpleEmail() {
    // this.emailService.sendEmailFromTemplateSync("tantruongspring@gmail.com",
    // "Test send email witt Template", "job");

    // return "OK";
    // }

    // Set cron job for every 60 s
    // @Scheduled(cron = "*/60 * * * * *")
    // @Transactional
    @GetMapping
    public String sendSubscriberEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "OK";
    }
}
