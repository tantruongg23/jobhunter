package vn.hoidanit.jobhunter.service;

import vn.hoidanit.jobhunter.domain.Subscriber;

public interface SubscriberService {
    Subscriber create(Subscriber subscriber);

    Subscriber update(Subscriber subscriber);

    Subscriber findByEmail(String email);

    void sendSubscribersEmailJobs();

}
