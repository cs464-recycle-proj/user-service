// package com.greenloop.user_service.services;

// import java.util.stream.Collectors;

// import org.springframework.stereotype.Service;

// import com.greenloop.user_service.dtos.EventDTO;
// import com.greenloop.user_service.dtos.EventRecommendationDTO;
// import com.greenloop.user_service.models.UserInterest;
// import com.greenloop.user_service.repos.UserInterestRepository;

// import java.util.*;

// @Service
// public class RecommendationService {

//     private final UserInterestRepository interestRepo;

//     public RecommendationService(UserInterestRepository interestRepo) {
//         this.interestRepo = interestRepo;
//     }

//     // Recommendation: simple rule-based (returns event IDs of interest categories)
//     public List<EventRecommendationDTO> recommendEvents(UUID userId, List<EventDTO> upcomingEvents) {

//         List<UserInterest> interests = interestRepo.findByUserId(userId);
//         List<String> categories = interests.stream().map(UserInterest::getInterest).collect(Collectors.toList());

//         // return upcomingEvents.stream()
//         //         .map(event -> new EventRecommendationDTO(event.getId(), computeScore(categories, event)))
//         //         .sorted(Comparator.comparingDouble(EventRecommendationDTO::getScore).reversed())
//         //         .limit(3)
//         //         .map(EventRecommendationDTO::getId)
//         //         .collect(Collectors.toList());
//         return upcomingEvents.stream()
//                 .map(event -> new EventRecommendationDTO(event.getId(), computeScore(categories, event)))
//                 .sorted(Comparator.comparingDouble(EventRecommendationDTO::getScore).reversed())
//                 .limit(3)
//                 // .map(EventRecommendationDTO::getId)
//                 .collect(Collectors.toList());

//     }

//     private double computeScore(List<String> interests, EventDTO event) {

//         Set<String> lowerInterests = interests.stream()
//                 .map(String::toLowerCase)
//                 .collect(Collectors.toSet());

//         long tagMatches = event.getTags().stream()
//                 .map(String::toLowerCase)
//                 .filter(lowerInterests::contains)
//                 .count();

//         double tagScore = tagMatches * 2;
//         if (tagScore == 0) {
//             return 0;
//         }
//         double popularityScore = event.getAttendeeCount() * 0.1;
//         return tagScore + popularityScore;
//     }

// }
