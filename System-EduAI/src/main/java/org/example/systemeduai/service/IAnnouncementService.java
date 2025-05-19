package org.example.systemeduai.service;

import org.example.systemeduai.dto.announcement.AnnouncementDTO;
import org.example.systemeduai.dto.nutrition.CreateReminderRequest;

public interface IAnnouncementService {
    AnnouncementDTO sendMealReminder(CreateReminderRequest request);
}
