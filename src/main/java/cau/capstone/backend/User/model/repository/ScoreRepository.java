package cau.capstone.backend.User.model.repository;

import cau.capstone.backend.User.model.Category;
import cau.capstone.backend.User.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Score findByUserId(Long userId);

    @Query("SELECT (s.tripScore + s.itnscienceScore + s.moviedramaScore + s.humorScore + " +
            "s.musicScore + s.marriageScore + s.romanceScore + s.cookingScore + " +
            "s.healthScore + s.studyingScore + s.artScore + s.animalScore + " +
            "s.humanityScore + s.literatureScore + s.financeScore) " +
            "FROM Score s WHERE s.user.id = :userId")
    int getTotalScoreByUserId(Long userId);

    //내부 쿼리에서 최대 점수를 갖는 카테고리를 얻고, 외부 쿼리에서 해당 카테고리를 반환
    @Query(value = "SELECT CASE" +
            " WHEN s.trip_score = max_score THEN 'TRIP' " +
            " WHEN s.itnscience_score = max_score THEN 'ITNSCI' " +
            " WHEN s.moviedrama_score = max_score THEN 'MVD' " +
            " WHEN s.humor_score = max_score THEN 'HUMR' " +
            " WHEN s.music_score = max_score THEN 'MUS' " +
            " WHEN s.marriage_score = max_score THEN 'MRG' " +
            " WHEN s.romance_score = max_score THEN 'ROM' " +
            " WHEN s.cooking_score = max_score THEN 'COK' " +
            " WHEN s.health_score = max_score THEN 'HLTH' " +
            " WHEN s.studying_score = max_score THEN 'STD' " +
            " WHEN s.art_score = max_score THEN 'ART' " +
            " WHEN s.animal_score = max_score THEN 'ANML' " +
            " WHEN s.humanity_score = max_score THEN 'HUMN' " +
            " WHEN s.literature_score = max_score THEN 'LIT' " +
            " WHEN s.finance_score = max_score THEN 'FIN' " +
            " END AS max_category " +
            " FROM (SELECT user_id, " +
            " GREATEST(trip_score, itnscience_score, moviedrama_score, humor_score, music_score, marriage_score, romance_score, cooking_score, health_score, studying_score, art_score, animal_score, humanity_score, literature_score, finance_score) AS max_score" +
            " FROM score WHERE user_id = :userId) AS max_scores " +
            " JOIN score s ON s.user_id = max_scores.user_id " +
            " WHERE max_score IN (s.tripScore, s.itnscienceScore, s.moviedramaScore, s.humorScore, s.musicScore, s.marriageScore, s.romanceScore, s.cookingScore, s.healthScore, s.studyingScore, s.artScore, s.animalScore, s.humanityScore, s.literatureScore, s.financeScore)", nativeQuery = true)
    String findMaxScoreCategoryByUserId(@Param("userId") Long userId);


    @Query(value = "SELECT s.user_id as userId, " +
            "s.trip_score as tripScore, " +
            "s.itnscience_score as itnscienceScore, " +
            "s.moviedrama_score as moviedramaScore, " +
            "s.humor_score as humorScore, " +
            "s.music_score as musicScore, " +
            "s.marriage_score as marriageScore, " +
            "s.romance_score as romanceScore, " +
            "s.cooking_score as cookingScore, " +
            "s.health_score as healthScore, " +
            "s.studying_score as studyingScore, " +
            "s.art_score as artScore, " +
            "s.animal_score as animalScore, " +
            "s.humanity_score as humanityScore, " +
            "s.literature_score as literatureScore, " +
            "s.finance_score as financeScore " +
            "FROM score s WHERE s.user_id = :userId", nativeQuery = true)
    List<Map<String, Object>> findScoresByUserId(@Param("userId") Long userId);
}
