package com.talengu.wordwarrior.model;

public class Eword {
    private int word_id;
    private String word_spell;
   // private String word_meaning;
    private int isOK;

    public Eword() {
        super();
    }

    public Eword(int word_id, String word_spell, String word_meaning, int isOK) {
        super();
        this.word_id = word_id;
        this.word_spell = word_spell;
      //  this.word_meaning = word_meaning;
        this.isOK = isOK;
    }

    public int getId() {
        return word_id;
    }

    public void setId(int wid) {
        this.word_id = wid;
    }


    public String getWordSpell() {
        return word_spell;
    }

    public void setWordSpell(String wspell) {
        this.word_spell = wspell;
    }
//    public String getWordMeaning() {
//        return word_meaning;
//    }

//    public void setWordMeaning(String wmeaning) {
//        this.word_meaning = wmeaning;
//    }
    public int getIsOK() {
        return isOK;
    }

    public void setIsOK(int isok) {
        this.isOK = isok;
    }
}
