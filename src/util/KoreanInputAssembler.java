package util;


import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KoreanInputAssembler {
    // UTF-16
    private static final String[] INITIAL_SOUND = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
            "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    private static final String[] MEDIAL_SOUND = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ",
            "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
    private static final String[] FINAL_SOUND = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ",
            "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ",
            "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ",
            "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
    private static HashMap<String, Integer> initialMap;
    private static HashMap<String, Integer> medialMap;
    private static HashMap<String, Integer> finalMap;
    // QWERTY layout binding
    private static String[] QWERTY_BIND;

    boolean ignore;

    private enum Stage {
        NONE, INITIAL, MEDIAL, FINAL
    }

    private Stage stage;

    private int initialHolder;
    private int medialHolder;
    private int finalHolder;
    private String lastBak;
    boolean doubledFinal;

    public KoreanInputAssembler() {
        initialHolder = 0;
        medialHolder = 0;
        finalHolder = 0;
        lastBak = null;
        doubledFinal = false;
        ignore = false;
        stage = Stage.INITIAL;
        /// UTF-16 encoding:  0xac00 + 588 * initial_idx + 28 * medial_idx + final_idx
        initialMap = new HashMap<String, Integer>();
        int length = INITIAL_SOUND.length;
        for (int i = 0; i < length; ++i) {
            initialMap.put(INITIAL_SOUND[i], 588 * i);
        }

        medialMap = new HashMap<String, Integer>();
        length = MEDIAL_SOUND.length;
        for (int i = 0; i < length; ++i) {
            medialMap.put(MEDIAL_SOUND[i], 28 * i);
        }

        finalMap = new HashMap<String, Integer>();
        length = FINAL_SOUND.length;
        for (int i = 0; i < length; ++i) {
            finalMap.put(FINAL_SOUND[i], i);
        }

        // qwerty layout binding
        QWERTY_BIND = new String[128];
        for (int i = 0; i < QWERTY_BIND.length; ++i) {
            QWERTY_BIND[i] = "" + (char) i;
        }

        QWERTY_BIND['q'] = "ㅂ";
        QWERTY_BIND['w'] = "ㅈ";
        QWERTY_BIND['e'] = "ㄷ";
        QWERTY_BIND['r'] = "ㄱ";
        QWERTY_BIND['t'] = "ㅅ";

        QWERTY_BIND['y'] = "ㅛ";
        QWERTY_BIND['u'] = "ㅕ";
        QWERTY_BIND['i'] = "ㅑ";
        QWERTY_BIND['o'] = "ㅐ";
        QWERTY_BIND['p'] = "ㅔ";


        QWERTY_BIND['a'] = "ㅁ";
        QWERTY_BIND['s'] = "ㄴ";
        QWERTY_BIND['d'] = "ㅇ";
        QWERTY_BIND['f'] = "ㄹ";
        QWERTY_BIND['g'] = "ㅎ";
        QWERTY_BIND['h'] = "ㅗ";
        QWERTY_BIND['j'] = "ㅓ";
        QWERTY_BIND['k'] = "ㅏ";
        QWERTY_BIND['l'] = "ㅣ";

        QWERTY_BIND['z'] = "ㅋ";
        QWERTY_BIND['x'] = "ㅌ";
        QWERTY_BIND['c'] = "ㅊ";
        QWERTY_BIND['v'] = "ㅍ";
        QWERTY_BIND['b'] = "ㅠ";
        QWERTY_BIND['n'] = "ㅜ";
        QWERTY_BIND['m'] = "ㅡ";

        for (int i = 'A'; i <= (int) 'Z'; ++i) {
            QWERTY_BIND[i] = QWERTY_BIND[i | 0x20];
        }

        QWERTY_BIND['Q'] = "ㅃ";
        QWERTY_BIND['W'] = "ㅉ";
        QWERTY_BIND['E'] = "ㄸ";
        QWERTY_BIND['R'] = "ㄲ";
        QWERTY_BIND['T'] = "ㅆ";

        QWERTY_BIND['O'] = "ㅒ";
        QWERTY_BIND['P'] = "ㅖ";
    }

    // TODO: combination consonant and vowel
    public void input(StringBuffer targetHandle, KeyEvent e) {
        if (ignore) {
            return;
        }
        int in = (int) e.getKeyChar();
        // Space bar, tab, backspace, enter keys break combining
        if (in == 0x20 || in == '\t' || in == 8 || in == 0xa) {
            stage = Stage.INITIAL;
            return;
        }
        if ('a' > (in | 0x20) || (in | 0x20) > 'z') {
            return;
        }

        boolean cond = true;
        while (cond) {
            cond = false;
            Integer initialCode = null;
            Integer medialCode = null;
            Integer finalCode = null;
            int idx = targetHandle.length() - 1;
            switch (stage) {
                case INITIAL:
                    medialCode = medialMap.get(QWERTY_BIND[in]);
                    if (finalHolder != 0 && medialCode != null) {
                        targetHandle.setLength(idx);
                        targetHandle.append(lastBak);
                        lastBak = null;
                        targetHandle.append((char) (0xac00 + medialCode + initialMap.get(QWERTY_BIND[finalHolder])));
                        initialHolder = finalHolder;
                        medialHolder = in;
                        stage = Stage.FINAL;
                        doubledFinal = false;
                        finalHolder = 0;
                    } else if (finalHolder != 0 && idx >= 0) {
                        stage = Stage.FINAL;
                        cond = true;
                    } else {
                        initialHolder = 0;
                        medialHolder = 0;
                        targetHandle.append(QWERTY_BIND[in]);
                        initialHolder = in;
                        stage = Stage.MEDIAL;
                        finalHolder = 0;
                        doubledFinal = false;
                    }
                    break;
                case MEDIAL:
                    doubledFinal = false;
                    String key = targetHandle.substring(idx);
                    initialCode = initialMap.get(key);
                    medialCode = medialMap.get(key);
                    finalCode = finalMap.get(key);
                    if (medialCode != null) {
                        if (key.equals("ㅡ") && QWERTY_BIND[in].equals("ㅣ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅢ");
                            break;
                        } else if (key.equals("ㅗ") && QWERTY_BIND[in].equals("ㅏ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅘ");
                            break;
                        } else if (key.equals("ㅗ") && QWERTY_BIND[in].equals("ㅐ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅙ");
                            break;
                        } else if (key.equals("ㅗ") && QWERTY_BIND[in].equals("ㅣ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅚ");
                            break;
                        } else if (key.equals("ㅜ") && QWERTY_BIND[in].equals("ㅓ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅝ");
                            break;
                        } else if (key.equals("ㅜ") && QWERTY_BIND[in].equals("ㅔ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅞ");
                            break;
                        } else if (key.equals("ㅜ") && QWERTY_BIND[in].equals("ㅣ")) {
                            targetHandle.setLength(idx);
                            targetHandle.append("ㅟ");
                            break;
                        }
                    }
                    if (initialCode == null && finalCode == null) {
                        stage = Stage.INITIAL;
                        cond = true;
                        finalHolder = 0;
                        break;
                    }

                    medialCode = medialMap.get(QWERTY_BIND[in]);
                    if (medialCode != null) {
                        if (initialCode != null) {
                            targetHandle.setLength(idx);
                            targetHandle.append((char) (0xac00 + initialCode + medialCode));
                        } else {
                            if (finalCode <= 3) {
                                targetHandle.setLength(idx);
                                targetHandle.append("ㄱ");
                            } else if (finalCode <= 6) {
                                targetHandle.setLength(idx);
                                targetHandle.append("ㄴ");
                            } else if (finalCode <= 14) {
                                targetHandle.setLength(idx);
                                targetHandle.append("ㄹ");
                            } else if (finalCode <= 18) {
                                targetHandle.setLength(idx);
                                targetHandle.append("ㅂ");
                            } else {
                                stage = Stage.FINAL;
                                medialHolder = in;
                                finalHolder = 0;
                                cond = true;
                                break;
                            }
                            targetHandle.append((char) (0xac00 + initialMap.get(QWERTY_BIND[initialHolder]) + medialCode));

                        }
                        stage = Stage.FINAL;
                        finalHolder = 0;
                        medialHolder = in;
                        break;
                    } else if (QWERTY_BIND[initialHolder].equals("ㄱ") && QWERTY_BIND[in].equals("ㅅ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄳ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄴ") && QWERTY_BIND[in].equals("ㅈ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄵ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄴ") && QWERTY_BIND[in].equals("ㅎ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄶ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㅂ") && QWERTY_BIND[in].equals("ㅅ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㅄ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㄱ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄺ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅁ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄻ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅂ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄼ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅅ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄽ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅌ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄾ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅍ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㄿ");
                    } else if (QWERTY_BIND[initialHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅎ")) {
                        targetHandle.setLength(idx);
                        targetHandle.append("ㅀ");
                    } else {
                        targetHandle.append(QWERTY_BIND[in]);
                    }
                    initialHolder = in;
                    stage = Stage.MEDIAL;
                    finalHolder = 0;
                    break;
                case FINAL:
                    // 합성된 중성음 조합
                    String medialKey;
                    medialCode = medialMap.get(QWERTY_BIND[in]);
                    if (medialCode != null) {
                        if (QWERTY_BIND[medialHolder].equals("ㅡ") && QWERTY_BIND[in].equals("ㅣ")) {
                            medialKey = "ㅢ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅗ") && QWERTY_BIND[in].equals("ㅏ")) {
                            medialKey = "ㅘ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅗ") && QWERTY_BIND[in].equals("ㅐ")) {
                            medialKey = "ㅙ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅗ") && QWERTY_BIND[in].equals("ㅣ")) {
                            medialKey = "ㅚ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅜ") && QWERTY_BIND[in].equals("ㅓ")) {
                            medialKey = "ㅝ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅜ") && QWERTY_BIND[in].equals("ㅔ")) {
                            medialKey = "ㅞ";
                        } else if (QWERTY_BIND[medialHolder].equals("ㅜ") && QWERTY_BIND[in].equals("ㅣ")) {
                            medialKey = "ㅟ";
                        } else {
                            targetHandle.append(QWERTY_BIND[in]);
                            break;
                        }
                        targetHandle.setLength(idx);
                        targetHandle.append((char) (0xac00 + initialMap.get(QWERTY_BIND[initialHolder]) + medialMap.get(medialKey)));

                        
                        stage = Stage.FINAL;
                        break;
                    }
                    // 종성음 합성
                    finalCode = finalMap.get(QWERTY_BIND[in]);
                    if (finalCode == null) {
                        stage = Stage.INITIAL;
                        cond = true;
                        finalHolder = 0;
                        break;
                    }
                    char buf = targetHandle.charAt(idx);
                    if (finalHolder == 0) {
                        lastBak = targetHandle.substring(idx);
                        targetHandle.setLength(idx);
                        System.out.println(buf);
                        System.out.println(finalCode);
                        targetHandle.append((char) ((int) buf + finalCode));
                        stage = Stage.FINAL;
                        finalHolder = in;
                    } else if (!doubledFinal){
                        if (QWERTY_BIND[finalHolder].equals("ㄱ") && QWERTY_BIND[in].equals("ㅅ")) {
                            finalCode = finalMap.get("ㄳ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄴ") && QWERTY_BIND[in].equals("ㅈ")) {
                            finalCode = finalMap.get("ㄵ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄴ") && QWERTY_BIND[in].equals("ㅎ")) {
                            finalCode = finalMap.get("ㄶ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㅂ") && QWERTY_BIND[in].equals("ㅅ")) {
                            finalCode = finalMap.get("ㅄ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㄱ")) {
                            finalCode = finalMap.get("ㄺ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅁ")) {
                            finalCode = finalMap.get("ㄻ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅂ")) {
                            finalCode = finalMap.get("ㄼ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅅ")) {
                            finalCode = finalMap.get("ㄽ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅌ")) {
                            finalCode = finalMap.get("ㄾ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅍ")) {
                            finalCode = finalMap.get("ㄿ");
                        } else if (QWERTY_BIND[finalHolder].equals("ㄹ") && QWERTY_BIND[in].equals("ㅎ")) {
                            finalCode = finalMap.get("ㅀ");
                        } else {
                            stage = Stage.INITIAL;
                            finalHolder = 0;
                            cond = true;
                            break;
                        }
                        doubledFinal = true;
                        finalCode = finalCode - finalMap.get(QWERTY_BIND[finalHolder]);
                        lastBak = targetHandle.substring(idx);
                        finalHolder = in;
                        targetHandle.setLength(idx);
                        targetHandle.append((char) ((int) buf + finalCode));
                    } else {
                        targetHandle.append(QWERTY_BIND[in]);
                        stage = Stage.MEDIAL;
                        finalHolder = 0;
                        doubledFinal = false;
                        break;
                    }
                    stage = Stage.INITIAL;
                    break;
                default:
                    assert (true);
                    break;
            }
        }

    }

    private boolean isValid(char c) {
        return (0xac00 <= c && c <= 0xd7a3);
    }

    public void toggleActivation() {
        stage = Stage.INITIAL;
        initialHolder = 0;
        medialHolder = 0;
        finalHolder = 0;
        ignore = !ignore;
    }

    public boolean isActivated() {
        return !ignore;
    }


}
