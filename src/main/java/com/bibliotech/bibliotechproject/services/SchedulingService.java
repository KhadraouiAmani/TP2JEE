package com.bibliotech.bibliotechproject.services;

import com.bibliotech.bibliotechproject.models.BorrowingStatus;
import com.bibliotech.bibliotechproject.repositories.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@EnableScheduling
public class SchedulingService {
    @Autowired private BorrowingRepository borrowingRepository;

    // Runs at midnight every night
    @Scheduled(cron = "0 0 0 * * *")
    public void checkOverdueBorrowings() {
        borrowingRepository.findAll()
                .filter(b -> b.getStatus() == BorrowingStatus.ONGOING && b.getReturnDate().isBefore(LocalDate.now()))
                .flatMap(b -> {
                    b.setStatus(BorrowingStatus.OVERDUE);
                    return borrowingRepository.save(b);
                })
                .subscribe(); // In Reactive, you must subscribe or nothing happens!
    }
}