import java.util.ArrayList;                         // ใช้เก็บข้อมูล
import java.util.Collection;
import java.util.Collections;
import java.util.List;                                     // ใช้เก็บข้อมููล
import java.util.Random;                           // ใช้สุ่มมคำศัพท์
import javax.swing.JOptionPane;         // กล่องแจ้งเตือน
import javax.swing.Timer;

public class GuessGUI {
    // ตัวแปรตาม Class Diagram
    private int score;
    private int timeRemaining;
    private int limitAnswer;
    private int hintsRemaining;
    private WordQuestion currentQuestion;
            
    // คลังคำถาม และ Instance ของหน้าจอ GUI
    private List<WordQuestion> questionBank; // ตัวแปรที่เก็บคำศัพท์ คำใบ้ คะแนน และ part of ...
    private MainGame ui;
    private String maskedWord; // คำที่ถูกซ่อน เช่น "_ _ _ _"
    private char[] currentMask; // เก็บสถานะขีด _ และตัวอักษรที่ทายถูกแล้ว
    private Timer gameTimer;    // // สับสวิตช์นับเวลาถอยหลัง
    
    
    public GuessGUI() {
       
        // 1. เริ่มต้นค่าตัวแปรเบื้องต้น
        this.score = 0;
        this.limitAnswer = 5;
        this.hintsRemaining = 5;
        this.questionBank = new ArrayList<>();
        
        // 2. เรียกสร้างคลังคำถาม
        initQuestions();

        // 3. สร้างหน้าจอ UI และเปิดขึ้นมา
        this.ui = new MainGame();
        this.ui.setVisible(true);
        
        // 4. ผูกปุ่มบน GUI เเข้ากับเมธอดควบคุมในไฟล์นี้
        initController();
        
        // 5. โหลดคำถามข้อแรก
        loadNewQuestion();
    }
    
    // เมธอดสำหรับใส่คำศัพท์ลงคลังคำถาม (มีทั้ง VocabularyWord และ IdiomWord)
    private void initQuestions() {
        // --- ชุดคำศัพท์ทั่วไป (VocabularyWord) ---
        questionBank.add(new VocabularyWord("JAVA", "A high-level programming language", 10, "Noun"));
        questionBank.add(new VocabularyWord("COMPUTER", "An electronic device for processing data", 10, "Noun"));
        questionBank.add(new VocabularyWord("ALGORITHM", "A process or set of rules to be followed in calculations", 15, "Noun"));
        questionBank.add(new VocabularyWord("DEBUG", "To identify and remove errors from computer hardware or software", 15, "Verb"));
        questionBank.add(new VocabularyWord("EFFICIENT", "Achieving maximum productivity with minimum wasted effort", 12, "Adjective"));
        questionBank.add(new VocabularyWord("VARIABLE", "A element, feature, or factor that is liable to vary or change", 10, "Noun"));
        questionBank.add(new VocabularyWord("DATABASE", "A structured set of data held in a computer", 12, "Noun"));
        questionBank.add(new VocabularyWord("NETWORK", "A group of two or more computer systems linked together", 10, "Noun"));
        questionBank.add(new VocabularyWord("SECURITY", "The state of being free from danger or threat in digital systems", 12, "Noun"));
        questionBank.add(new VocabularyWord("SYNTAX", "The set of rules that defines the combinations of symbols in programming", 15, "Noun"));

        // --- ชุดสำนวนและสุภาษิต (IdiomWord) ---
        questionBank.add(new IdiomWord("PIECE OF CAKE", "Very easy to do", 20, "Used when something is extremely simple or effortless"));
        questionBank.add(new IdiomWord("BREAK A LEG", "Good luck", 20, "Typically said to actors or performers before they go on stage"));
        questionBank.add(new IdiomWord("ONCE IN A BLUE MOON", "Very rarely", 25, "Refers to an event that happens very infrequently"));
        questionBank.add(new IdiomWord("BITING THE BULLET", "Facing a difficult situation with courage", 25, "Forcing yourself to do something unpleasant or difficult"));
        questionBank.add(new IdiomWord("UNDER THE WEATHER", "Feeling sick or unwell", 20, "Used when someone is slightly ill or not feeling their best"));
        questionBank.add(new IdiomWord("CALL IT A DAY", "To stop working on something", 20, "Said when finishing a day's work or ending an activity"));
        questionBank.add(new IdiomWord("SPILL THE BEANS", "To reveal a secret", 25, "Used when someone accidentally or intentionally tells confidential info"));
        questionBank.add(new IdiomWord("BURN THE MIDNIGHT OIL", "To work late into the night", 25, "Often used when studying or working hard until early morning"));
        
        // แก้ปัญหาเปิดใหม่ได้คำเดิม: สลับลำดับคำถามใหม่ทุกครั้งที่เปิดโปรแกรม
        Collections.shuffle(questionBank);
    }
    
    // เมธอดที่ผูก Event ให้ปุ่มบน UI
    private void initController() {
        ui.getBtnSubmint().addActionListener(e -> handleSubmit());
        ui.getBtnHint().addActionListener(e -> handleHint());
        ui.getbtnNextLevel().addActionListener(e  -> handleNext());
    }
    
    // เมธอดโหลดคำถามข้อใหม่ (สุ่มจาก questionBank)
    public void loadNewQuestion() {
        if (questionBank.isEmpty()) {
            JOptionPane.showMessageDialog(ui, "Congratulations! You completed all questions! " );
            return;
        }
        
        // สุ่มคำถาม 1 ข้อ
        Random rand = new Random();
        int index = rand.nextInt(questionBank.size()); // .size() ก็คือ .lenght ใน Array ปกติ
        currentQuestion = questionBank.get(index);
        
        // ตั้งค่าเริ่มต้นของข้อใหม่
        limitAnswer = 5;

        // ============= [สำคัญ] =============
        // แปลงคำเฉลยให้กลายเป็นขีดล่าง _ _ _ _
        String secret = currentQuestion.getSecretWord().toUpperCase();
        currentMask = new char[secret.length()];
        
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == ' ') {
                currentMask[i] = ' ';
            } else {
                currentMask[i] = '_';
            }
        }
        
        // ล็อคปุ่ม Next ไว้จนกว่าจะถูก
        ui.setNextButtonEnabled(false);
        ui.clearInputAnswer();
        
        // เริ่มนับเวลาถอยหลัง
        startTimer();
        
        // อัพเดตข้อมมูลขึ้นจอ
        updateUI();
    }
    
    // เมธอดนับเวลาถอยหลัง (Timer)
    private void startTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        
        timeRemaining = 60; // กำหนดเวลา 60 วินาทีต่อข้อ
        gameTimer = new Timer(1000,  e -> {
            timeRemaining--;
            ui.setTimeText(String.valueOf(timeRemaining)); // แสดงเวลา
            
            if (timeRemaining <= 0) {
                gameTimer.stop();
                revealFullWord();
                JOptionPane.showMessageDialog(ui, "Time's up! The answer was: " + currentQuestion.getSecretWord());
                ui.setNextButtonEnabled(true);
                updateUI();
            }
        });
        gameTimer.start();
    }
    
    // เมธอดเมื่อผู้ใช้กดปุ่ม Submit
    public void handleSubmit() {
        
        if (limitAnswer <= 0 || isAllGuessed() || timeRemaining <= 0) {
            return;
        }
        
        String userAns = ui.getInputAnswer().trim().toUpperCase();
        ui.clearInputAnswer();
        
        
        if (userAns.trim().isEmpty()) {
            JOptionPane.showMessageDialog(ui, "Please enter your annser!");
            return;
        }
        
        String secret = currentQuestion.getSecretWord().toUpperCase();

        // กรณีที่ 1: ผู้เล่นพิมพ์มาตัวอักษรเดียว (ทายทีละตัว)
        if (userAns.length() == 1) {
            char guessedChar = userAns.charAt(0);
            boolean found = false;
            
            // แก้ไขจุดนี้: เปลี่ยนจาก <= เป็น <
            for (int i = 0; i < secret.length(); i++) {
                if (secret.charAt(i) == guessedChar && currentMask[i] == '_') {
                    currentMask[i] = guessedChar;
                    found = true;
                }
            }
            
            if (found) {
                if (isAllGuessed()) {
                    handleWin();
                }
            } else {
                limitAnswer--;
                JOptionPane.showMessageDialog(ui, "Wrong letter! Try again.");
            }
        }
        
        // กรณีที่ 2: ผู้เล่นพิมพ์ทายทั้งคำ
        else {
            if (currentQuestion.checkAnswer(userAns)) {
                revealFullWord();
                handleWin();
            } else {
                limitAnswer--;
                JOptionPane.showMessageDialog(ui, "Wrong word! Try again.");
            }
        }
        
        // กรณีโควตาหมด (แพ้)
        if (limitAnswer <= 0 && !isAllGuessed()) {
            if (gameTimer != null) gameTimer.stop();
            revealFullWord();
            JOptionPane.showMessageDialog(ui, "Game Over for this word! The answer was: " + currentQuestion.getSecretWord());
            ui.setNextButtonEnabled(true);
        }
        
        updateUI();
    }
    
    // เมธอดเมื่อทายถูก
    private void handleWin() {
        if (gameTimer != null) gameTimer.stop();
        score += currentQuestion.getRewardScore();
        JOptionPane.showMessageDialog(ui, "Correct! +" + currentQuestion.getRewardScore() + " points");
        ui.setNextButtonEnabled(true);
        questionBank.remove(currentQuestion ); // ลบข้อที่ผ่านแล้วออกจากคลัง
    }
    
    // เมธอดเช็คว่าเป็น "_" ไหม ถ้าเป็นให้คือค่า false
    private boolean isAllGuessed() {
        for (char c: currentMask) {
            if (c == '_') return false;
        }
        return true;
    } 
    
    // เมธอดเฉลยคำตอบ
    private void revealFullWord() {
        String secret = currentQuestion.getSecretWord().toUpperCase();
        currentMask = secret.toCharArray();
    }
    
    // เมธอดที่ที่ผู้กด Hint
    public void handleHint() {
        if (hintsRemaining > 0) {
            hintsRemaining--;
            
            // เรียกใช้ getFormattedHint() ซึ่งเป็น Polymorphism (แสดงผลต่างกันตามประเภทคำถาม)
            // JOptionPane.INFORMATION_MESSAGE จะแสดงรูปตัวℹ️ (ตัว i สีฟ้า) เพื่อแจ้งข้อมูลทั่วไปให้ผู้ใช้ทราบ 
            JOptionPane.showMessageDialog(ui, currentQuestion.getFormattedHint(), "Hint", JOptionPane.INFORMATION_MESSAGE); 
            updateUI();
        } else {
            JOptionPane.showMessageDialog(ui, "No hints remaining");
        }
    }
    
    // เมธอดที่จะทำงานเมื่อกดปุ่ม Next
    public void handleNext() { loadNewQuestion(); }
    
    // แปลงอาร์เรย์ char[] ให้อยู่ในรูปข้อความพร้อมเว้นวรรค เช่น "J A V A"    
    private String getFormattedMaskedWord() {
        StringBuffer sb = new StringBuffer();
        // แก้ไขจุดนี้: เปลี่ยนจาก sb.append(sb) เป็น sb.append(c)
        for (char c : currentMask) {
            sb.append(c).append(" ");
        }
        
        return sb.toString().trim(); // ใช้ trim() ตัดช่องว่างซ้ายขวา
    }
    
    // [!สำคัญ!]
    // เมธอดอัปเดตข้อความบนหน้าจอ GUI ทุกอย่างให้เป็นปัจจุบัน
    public void updateUI() {
        ui.setScoreText(String.valueOf(score));                                         // set คะแนน
        ui.setTimeText(String.valueOf(timeRemaining));                     // set เวลา
        ui.setLimitAnswerText(String.valueOf((limitAnswer)));       // set ลิมิตในการตอบ
        ui.setHintText("Hints remaining : " + hintsRemaining);       // set จำนวนที่ตอบได้
        ui.setGuessWordText(getFormattedMaskedWord());
        
        // ถ้ายังทายไม่ถูก ให้โชว์คำซ่อน (_ _ _) แต่ถ้าผ่านแล้วให้โชว์คำเฉลย
//        if (limitAnswer > 0 && !ui.getInputAnswer().equalsIgnoreCase(currentQuestion.getSecretWord())) {
//            ui.setGuessWordText(maskedWord);
//        }
        
    }  
    // Main Method สำหรับเริ่มรันโปรแกรมจากไฟล์นี้
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new GuessGUI());
    }
}
