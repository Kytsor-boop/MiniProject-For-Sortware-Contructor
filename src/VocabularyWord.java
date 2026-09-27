/* 
เราสร้างคลาสที่เก็บคำศัพท์, คำใบ้, คะแนนของคำนั้น และ pastOfSpeech 
เพื่อที่ว่าเวลาเพิ่มคำใหม่ในตัวแปร questionBank มันจะมาเป็นข้อมูลของชุดคำนั้นทั้งหมดเลย 
เพราะ questionBank เป็น List ที่จะเก็บข้อมูลของคำนั้นๆไว้
เหมือนแบบคำนี้มีข้อนมูลนี้ๆ ว่ากันไป
*/
public class VocabularyWord extends WordQuestion {
    
    // Encapsulation : กำหนดให้ pastOfSpeech เป็น private
    private String partOfSpeech;
    
    // Contructor เรียกใช้ super() เพื่อส่งค่าไปยังคลาสแม่
    public VocabularyWord(String secretWord, String baseHint, int rewardScore, String partOfSpeech) {
        super(secretWord, baseHint, rewardScore);
        this.partOfSpeech = partOfSpeech;
    }
    
    // Getter
    public String  getPartOfSpeech() {
        return partOfSpeech;
    }
    
    // Polymorhism : @Override ปรับรูปแบบคำใบ้ให้โชว์ชนิดของคำ
    @Override
    public String getFormattedHint() {
        return "[" + partOfSpeech + "]" + getBaseHint();
    }     
}
