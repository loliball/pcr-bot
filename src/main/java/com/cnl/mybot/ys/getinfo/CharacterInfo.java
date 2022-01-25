package com.cnl.mybot.ys.getinfo;


public class CharacterInfo {
    private int id;
    private String image;
    private String name;
    private String element;
    private int fetter;
    private int level;
    private int rarity;

    @Override
    public String toString() {
        return String.format("%s(%d级，好感%d)",
                name, level, fetter, rarity, Element.getElementName(element));
    }
}
