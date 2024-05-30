package cau.capstone.backend.User.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.User.model.Score;
import cau.capstone.backend.User.model.repository.ScoreRepository;
import cau.capstone.backend.page.model.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    private final int upperLimit = 1000;


    //페이지에 좋아요를 누를 때 카테고리 정보를 받아 유저의 해당 카테고리 점수를 업데이트
    public void plusLikeScore(Long userId, Page page){
        Book book = page.getBook();
        Category category = book.getCategory();

        Score score = scoreRepository.findByUserId(userId);

        checkUpper(score, userId);

//category_code = {TRIP, ITNSCI, MVD, HUMR, MUS, MRG, ROM, COK, HLTH, STD, ART, ANML, HUMN, LIT, FIN}
        switch (category.getCode()){
            case "TRIP" :
                score.setTripScore(score.getTripScore() + 10);
                break;

            case "ITNSCI" :
                score.setItnscienceScore(score.getItnscienceScore() + 10);
                break;

            case "MVD" :
                score.setMoviedramaScore(score.getMoviedramaScore() + 10);
                break;

            case "HUMR" :
                score.setHumorScore(score.getHumorScore() + 10);
                break;

            case "MUS" :
                score.setMusicScore(score.getMusicScore() + 10);
                break;

            case "MRG" :
                score.setMarriageScore(score.getMarriageScore() + 10);
                break;

            case "ROM" :
                score.setRomanceScore(score.getRomanceScore() + 10);
                break;

            case "COK" :
                score.setCookingScore(score.getCookingScore() + 10);
                break;

            case "HLTH" :
                score.setHealthScore(score.getHealthScore() + 10);
                break;

            case "STD" :
                score.setStudyingScore(score.getStudyingScore() + 10);
                break;

            case "ART" :
                score.setArtScore(score.getArtScore() + 10);
                break;

            case "ANML" :
                score.setAnimalScore(score.getAnimalScore() + 10);
                break;

            case "HUMN" :
                score.setHumanityScore(score.getHumanityScore() + 10);
                break;

            case "LIT" :
                score.setLiteratureScore(score.getLiteratureScore() + 10);
                break;

            case "FIN" :
                score.setFinanceScore(score.getFinanceScore() + 10);
                break;

            default:
                break;
        }
        scoreRepository.save(score);

    }

    public void plusViewScore(Long userId, Page page){

        Category category = page.getBook().getCategory();

        Score score = scoreRepository.findByUserId(userId);

        checkUpper(score, userId);
//category_code = {TRIP, ITNSCI, MVD, HUMR, MUS, MRG, ROM, COK, HLTH, STD, ART, ANML, HUMN, LIT, FIN}
        switch (category.getCode()){
            case "TRIP" :
                score.setTripScore(score.getTripScore() + 1);
                System.out.println("trip");
                break;
            case "ITNSCI" :
                score.setItnscienceScore(score.getItnscienceScore() + 1);
                System.out.println("itnsci");
                break;

            case "MVD" :
                score.setMoviedramaScore(score.getMoviedramaScore() + 1);
                System.out.println("mvd");
                break;

            case "HUMR" :
                score.setHumorScore(score.getHumorScore() + 1);
                System.out.println("humr");
                break;

            case "MUS" :
                score.setMusicScore(score.getMusicScore() + 1);
                System.out.println("mus");
                break;

            case "MRG" :
                score.setMarriageScore(score.getMarriageScore() + 1);
                System.out.println("mrg");
                break;

            case "ROM" :
                score.setRomanceScore(score.getRomanceScore() + 1);
                System.out.println("rom");
                break;

            case "COK" :
                score.setCookingScore(score.getCookingScore() + 1);
                System.out.println("cok");
                break;

            case "HLTH" :
                score.setHealthScore(score.getHealthScore() + 1);
                System.out.println("hlth");
                break;

            case "STD" :
                score.setStudyingScore(score.getStudyingScore() + 1);
                System.out.println("std");
                break;

            case "ART" :
                score.setArtScore(score.getArtScore() + 1);
                System.out.println("art");
                break;

            case "ANML" :
                score.setAnimalScore(score.getAnimalScore() + 1);
                System.out.println("anml");
                break;

            case "HUMN" :
                score.setHumanityScore(score.getHumanityScore() + 1);
                System.out.println("humn");
                break;

            case "LIT" :
                score.setLiteratureScore(score.getLiteratureScore() + 1);
                System.out.println("lit");
                break;

            case "FIN" :
                score.setFinanceScore(score.getFinanceScore() + 1);
                System.out.println("fin");
                break;

            default:
                break;
        }
        scoreRepository.save(score);

    }



    //유저 개인의 점수 총합이 상한에 도달하면 전부 1/2로 나누기, 나머지 버림 (상한 = 1000점)
    public void checkUpper(Score score, Long userId) {
        int totalScore = scoreRepository.getTotalScoreByUserId(userId);

        if(totalScore >= upperLimit){

            score.setTripScore(score.getTripScore() / 2);
            score.setItnscienceScore(score.getItnscienceScore() / 2);
            score.setMoviedramaScore(score.getMoviedramaScore() / 2);
            score.setHumorScore(score.getHumorScore() / 2);
            score.setMusicScore(score.getMusicScore() / 2);
            score.setMarriageScore(score.getMarriageScore() / 2);
            score.setRomanceScore(score.getRomanceScore() / 2);
            score.setCookingScore(score.getCookingScore() / 2);
            score.setHealthScore(score.getHealthScore() / 2);
            score.setStudyingScore(score.getStudyingScore() / 2);
            score.setArtScore(score.getArtScore() / 2);
            score.setAnimalScore(score.getAnimalScore() / 2);
            score.setHumanityScore(score.getHumanityScore() / 2);
            score.setLiteratureScore(score.getLiteratureScore() / 2);
            score.setFinanceScore(score.getFinanceScore() / 2);

            scoreRepository.save(score);

        }
    }


    public List<Map.Entry<String, Integer>> getSortedScoresByUserId(String accessToken) {
        User user = userRepository.findByEmail(jwtTokenProvider.getUserEmail(accessToken))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long userId = user.getId();

        List<Map<String, Object>> scores = scoreRepository.findScoresByUserId(userId);

        if (scores.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> scoreMap = new HashMap<>();
        Map<String, Object> score = scores.get(0);

        scoreMap.put("TRIP", (Integer) score.get("tripScore"));
        scoreMap.put("ITNSCIENCE", (Integer) score.get("itnscienceScore"));
        scoreMap.put("MOVIEDRAMA", (Integer) score.get("moviedramaScore"));
        scoreMap.put("HUMOR", (Integer) score.get("humorScore"));
        scoreMap.put("MUSIC", (Integer) score.get("musicScore"));
        scoreMap.put("MARRIAGE", (Integer) score.get("marriageScore"));
        scoreMap.put("ROMANCE", (Integer) score.get("romanceScore"));
        scoreMap.put("COOKING", (Integer) score.get("cookingScore"));
        scoreMap.put("HEALTH", (Integer) score.get("healthScore"));
        scoreMap.put("STUDYING", (Integer) score.get("studyingScore"));
        scoreMap.put("ART", (Integer) score.get("artScore"));
        scoreMap.put("ANIMAL", (Integer) score.get("animalScore"));
        scoreMap.put("HUMANITY", (Integer) score.get("humanityScore"));
        scoreMap.put("LITERATURE", (Integer) score.get("literatureScore"));
        scoreMap.put("FINANCE", (Integer) score.get("financeScore"));

        return scoreMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
}