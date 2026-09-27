public class WordQuestion {
    // Encapsulation : ทำการซ่อนให้เป็น private เพื่อให้แก้ไขไม่ได้
    private String secretWord, baseHint;
    private int rewardScore;
    
    // สร้าง Constructor รับค่าเริ่มต้น
    public WordQuestion(String secretWord, String baseHint, int rewardScore) {
        this.secretWord = secretWord;
        this.baseHint = baseHint;
        this.rewardScore = rewardScore;
    }
    
    // Getter ดึงข้อมูล
    public String getSecretWord() {
        return secretWord;
    }
    
    public String getBaseHint() {
        return baseHint;
    }
    
    public int getRewardScore() {
        return rewardScore;
    }
    
    /* 
    Method สำหรับตรวจคำตอบ 
          - เทียบโดยไม่สนตัวพิพ์เล็กหรือใหญ่
          - ตัดช่องว่างหน้าหลังออก
    */
    public boolean checkAnswer(String userAns) {
        
        if (userAns == null) return false;
        return secretWord.trim().equalsIgnoreCase(userAns.trim());
    }
    /*
    Method สำหรับ Polymorphism
            - เปิดไว้ใหคลาสลูกนำไป Override
    */
    public String getFormattedHint() {
        return "Hint : " + baseHint;
    }
    
}
