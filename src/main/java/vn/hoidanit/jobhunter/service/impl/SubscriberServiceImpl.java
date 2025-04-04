package vn.hoidanit.jobhunter.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.dto.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository,
            SkillRepository skillRepository,
            EmailService emailService,
            JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Subscriber create(Subscriber subscriber) {

        boolean isEmailExists = this.subscriberRepository.existsByEmail(subscriber.getEmail());
        if (isEmailExists) {
            throw new IdInvalidException("Email is existed");
        }

        if (subscriber.getSkills() != null) {
            List<Long> ids = new LinkedList<>();
            for (Skill skill : subscriber.getSkills()) {
                ids.add(skill.getId());
            }

            List<Skill> skills = this.skillRepository.findAllById(ids);
            subscriber.setSkills(skills);
        }

        return this.subscriberRepository.save(subscriber);

    }

    @Override
    public Subscriber update(Subscriber subscriber) {
        Subscriber createdSubscriber = findById(subscriber.getId());

        if (subscriber.getSkills() != null) {
            List<Long> ids = new LinkedList<>();
            for (Skill skill : subscriber.getSkills()) {
                ids.add(skill.getId());
            }

            List<Skill> skills = this.skillRepository.findByIdIn(ids);

            subscriber.setSkills(skills);
        }

        createdSubscriber.setSkills(subscriber.getSkills());

        return this.subscriberRepository.save(createdSubscriber);

    }

    @Override
    public void sendSubscribersEmailJobs() {
        List<Subscriber> subscribers = this.subscriberRepository.findAll();
        if (subscribers != null && !subscribers.isEmpty()) {
            for (Subscriber subscriber : subscribers) {
                List<Skill> skills = subscriber.getSkills();
                if (skills != null && !skills.isEmpty()) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(skills);
                    if (jobs != null && !jobs.isEmpty()) {
                        List<ResEmailJob> res = jobs.stream()
                                .map(
                                        job -> this.convertJobToSendEmail(job))
                                .collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                subscriber.getEmail(),
                                "Cơ hội việc làm đang chờ đón bạn, khám phá ngay",
                                "job",
                                subscriber.getName(),
                                res);
                    }
                }
            }
        }
    }

    // @Scheduled(fixedDelay = 5000)
    // public void testCron() {
    // System.out.println(">>>>Test Cron");
    // }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        ResEmailJob.CompanyEmail companyEmail = new ResEmailJob.CompanyEmail(job.getCompany().getName());
        List<ResEmailJob.SkillEmail> skillEmails = job.getSkills().stream().map(
                skill -> new ResEmailJob.SkillEmail(skill.getName())).collect(Collectors.toList());

        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(companyEmail);
        res.setSkills(skillEmails);

        return res;
    }

    public Subscriber findById(long id) {
        return this.subscriberRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Subscriber not found for ID = " + id));
    }
}
