package com.littlebear.learnhanzi.ui.learning;

import java.io.Serializable;

class Textbook  implements Serializable{
    int textbookNo;
    String textbookTitle;
    String bookDir;

}

class Lesson  implements Serializable {
    String lessonTitle;
    String jsonFile;
    int lessonNo;
    int characterNo;
    int readingNo;

}

class Reading implements Serializable {
    String readingTitle;
    String readingText;
}

enum HanziPage {PRONUNCIATION, WRITING, WORD, SENTENCE, READING};

