
public class IdiomWord extends WordQuestion {
    // Encapsulation : กำหนด contextMeaning เป็น private
    private String contextMeaning;
    
    // เรียกใช้ Contructor ของคลาสแม่ด้วย super()
    public IdiomWord(String secretWord, String baseHint, int rewardScore, String partOfSpeech) {
        super(secretWord, baseHint, rewardScore);
        this.contextMeaning = contextMeaning;
    }
    public String getContextMeaning() {
        return contextMeaning;
    }
    
    // Polymorphism: @Override ปรับรูปแบบคำใบ้ให้โชว์บริบทตัวอย่าง
    @Override
    public String getFormattedHint() {
        return "[Idiom] " + getBaseHint() + "  (Note : " + contextMeaning + ")";
    }
    
}
