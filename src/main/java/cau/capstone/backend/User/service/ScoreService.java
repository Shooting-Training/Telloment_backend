package cau.capstone.backend.User.service;

import cau.capstone.backend.User.model.Category;
import cau.capstone.backend.User.model.Score;
import cau.capstone.backend.User.model.repository.ScoreRepository;
import cau.capstone.backend.page.model.Page;
import lombok.AllArgsConstructor;
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


    private final int upperLimit = 100;


    //페이지에 좋아요를 누를 때 카테고리 정보를 받아 유저의 해당 카테고리 점수를 업데이트
    public void plusScore(Long userId, Page page){

        checkUpper(userId);

        Category category = page.getCategory();
        Score score = new Score();
//category_code = {TRIP, ITNSCI, MVD, HUMR, MUS, MRG, ROM, COK, HLTH, STD, ART, ANML, HUMN, LIT, FIN}
        switch (category.getCode()){
            case "TRIP" :
               score =  scoreRepository.findByUserId(userId);
               score.setTripScore(score.getTripScore() + 1);
                scoreRepository.save(score);
            case "ITNCSI" :
                score =  scoreRepository.findByUserId(userId);
                score.setItnscienceScore(score.getItnscienceScore() + 1);
                scoreRepository.save(score);
            case "MVD" :
                score =  scoreRepository.findByUserId(userId);
                score.setMoviedramaScore(score.getMoviedramaScore() + 1);
                scoreRepository.save(score);
            case "HUMR" :
                score =  scoreRepository.findByUserId(userId);
                score.setHumorScore(score.getHumorScore() + 1);
                scoreRepository.save(score);
            case "MUS" :
                score =  scoreRepository.findByUserId(userId);
                score.setMusicScore(score.getMusicScore() + 1);
                scoreRepository.save(score);
            case "MRG" :
                score =  scoreRepository.findByUserId(userId);
                score.setMarriageScore(score.getMarriageScore() + 1);
                scoreRepository.save(score);
            case "ROM" :
                score =  scoreRepository.findByUserId(userId);
                score.setRomanceScore(score.getRomanceScore() + 1);
                scoreRepository.save(score);
            case "COK" :
                score =  scoreRepository.findByUserId(userId);
                score.setCookingScore(score.getCookingScore() + 1);
                scoreRepository.save(score);
            case "HLTH" :
                score =  scoreRepository.findByUserId(userId);
                score.setHealthScore(score.getHealthScore() + 1);
                scoreRepository.save(score);
            case "STD" :
                score =  scoreRepository.findByUserId(userId);
                score.setStudyingScore(score.getStudyingScore() + 1);
                scoreRepository.save(score);
            case "ART" :
                score =  scoreRepository.findByUserId(userId);
                score.setArtScore(score.getArtScore() + 1);
                scoreRepository.save(score);
            case "ANML" :
                score =  scoreRepository.findByUserId(userId);
                score.setAnimalScore(score.getAnimalScore() + 1);
                scoreRepository.save(score);
            case "HUMN" :
                score =  scoreRepository.findByUserId(userId);
                score.setHumanityScore(score.getHumanityScore() + 1);
                scoreRepository.save(score);
            case "LIT" :
                score =  scoreRepository.findByUserId(userId);
                score.setLiteratureScore(score.getLiteratureScore() + 1);
                scoreRepository.save(score);
            case "FIN" :
                score =  scoreRepository.findByUserId(userId);
                score.setFinanceScore(score.getFinanceScore() + 1);
                scoreRepository.save(score);
            default:
                break;
        }

    }



    //유저 개인의 점수 총합이 상한에 도달하면 전부 1/2로 나누기, 나머지 버림 (상한 = 100점)
    public void checkUpper(Long userId){
        int totalScore = scoreRepository.getTotalScoreByUserId(userId);

        if(totalScore >= upperLimit){
            Score score = scoreRepository.findByUserId(userId);

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


    public List<Map.Entry<String, Integer>> getSortedScoresByUserId(Long userId) {
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
