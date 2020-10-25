package sample;

public class Word {
    public int id;
    public String eng;
    public String translation;

    public Word(){
        id = 0;
        eng = null;
        translation = null;
    }

    public Word(int id, String eng, String translation){
        this.id = id;
        this.eng = eng;
        this.translation = translation;
    }

}
