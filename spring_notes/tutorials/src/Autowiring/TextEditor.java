package Autowiring;

import org.springframework.beans.factory.annotation.Autowired;

public class TextEditor {

    private SpellChecker spellChecker;

    @Autowired
    public TextEditor(SpellChecker spellChecker) {
        System.out.println("Inside TextEditor constructor.");
        this.spellChecker = spellChecker;
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    public void spellCheck() {
        spellChecker.checkSpelling();
    }
}
