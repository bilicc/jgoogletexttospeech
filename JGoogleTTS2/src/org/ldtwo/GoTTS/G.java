/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ldtwo.GoTTS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author ldtwo
 */
public class G {

    public static boolean CONFIRM_CLOSE = false;
    public static final int[] FONT_SIZES = {5, 8, 10, 11, 12, 14, 18, 24, 36, 48};
    public final static Random RAND = new SecureRandom();
    public static HashSet<File> openFiles = new HashSet<File>();
    public static LinkedList<EditorPanel> tabList = new LinkedList<EditorPanel>();
    public static long delay = 1;
    public static final String DELIM = "[\\n]", ANTIDELIM = "[^\\n]";
    public static final String DELIM2 = "[,.\\n]", ANTIDELIM2 = "[^,.\\n]";
    public static boolean play = true;
    public static boolean pause = false;
    public final static boolean SIMULATION = false;
    static final HashMap<String, String> LA_LANGUAGE = new HashMap<String, String>();
    static final HashMap<String, String> LANGUAGE_LA = new HashMap<String, String>();
    public final static String[] LANGS = {
        "af	Afrikaans",
        "sq	Albanian",
        "am	Amharic",
        "ar	Arabic",
        "hy	Armenian",
        "az	Azerbaijani",
        "eu	Basque",
        "be	Belarusian",
        "bn	Bengali",
        "bh	Bihari",
        "bs	Bosnian",
        "br	Breton",
        "bg	Bulgarian",
        "km	Cambodian",
        "ca	Catalan",
        "zh-CN	Chinese (Simplified)",
        "zh-TW	Chinese (Traditional)",
        "co	Corsican",
        "hr	Croatian",
        "cs	Czech",
        "da	Danish",
        "nl	Dutch",
        "en	English (US)",
        "en_gb	English (GB)",
        "en_au	English (AU)",
        "eo	Esperanto",
        "et	Estonian",
        "fo	Faroese",
        "tl	Filipino",
        "fi	Finnish",
        "fr	French",
        "fy	Frisian",
        "gl	Galician",
        "ka	Georgian",
        "de	German",
        "el	Greek",
        "gn	Guarani",
        "gu	Gujarati",
        "xx-hacker	Hacker (Google hoax)",
        "ha	Hausa",
        "iw	Hebrew",
        "hi	Hindi",
        "hu	Hungarian",
        "is	Icelandic",
        "id	Indonesian",
        "ia	Interlingua",
        "ga	Irish",
        "it	Italian",
        "ja	Japanese",
        "jw	Javanese",
        "kn	Kannada",
        "kk	Kazakh",
        "rw	Kinyarwanda",
        "rn	Kirundi",
        "xx-klingon	Klingon (Google hoax)",
        "ko	Korean",
        "ku	Kurdish",
        "ky	Kyrgyz",
        "lo	Laothian",
        "la	Latin",
        "lv	Latvian",
        "ln	Lingala",
        "lt	Lithuanian",
        "mk	Macedonian",
        "mg	Malagasy",
        "ms	Malay",
        "ml	Malayalam",
        "mt	Maltese",
        "mi	Maori",
        "mr	Marathi",
        "mo	Moldavian",
        "mn	Mongolian",
        "sr-ME	Montenegrin",
        "ne	Nepali",
        "no	Norwegian",
        "nn	Norwegian (Nynorsk)",
        "oc	Occitan",
        "or	Oriya",
        "om	Oromo",
        "ps	Pashto",
        "fa	Persian",
        "xx-pirate	Pirate (Google hoax)",
        "pl	Polish",
        "pt-BR	Portuguese (Brazil)",
        "pt-PT	Portuguese (Portugal)",
        "pa	Punjabi",
        "qu	Quechua",
        "ro	Romanian",
        "rm	Romansh",
        "ru	Russian",
        "gd	Scots Gaelic",
        "sr	Serbian",
        "sh	Serbo-Croatian",
        "st	Sesotho",
        "sn	Shona",
        "sd	Sindhi",
        "si	Sinhalese",
        "sk	Slovak",
        "sl	Slovenian",
        "so	Somali",
        "es	Spanish",
        "su	Sundanese",
        "sw	Swahili",
        "sv	Swedish",
        "tg	Tajik",
        "ta	Tamil",
        "tt	Tatar",
        "te	Telugu",
        "th	Thai",
        "ti	Tigrinya",
        "to	Tonga",
        "tr	Turkish",
        "tk	Turkmen",
        "tw	Twi",
        "ug	Uighur",
        "uk	Ukrainian",
        "ur	Urdu",
        "uz	Uzbek",
        "vi	Vietnamese",
        "cy	Welsh",
        "xh	Xhosa",
        "yi	Yiddish",
        "yo	Yoruba",
        "zu	Zulu"};
    //static int activeTabNum=0;

    static {
        String[] arr;
        for (String s : LANGS) {
            arr = s.split("\t");
            LA_LANGUAGE.put(arr[0], arr[1]);
            LANGUAGE_LA.put(arr[1], arr[0]);
        }

    }

    public static boolean saveFile(EditorPanel p, boolean askForFileName) {
        if (p.file == null||askForFileName) {
            int selectedIndex = Frame2.ths.tabPane.getSelectedIndex();
            Frame2.ths.tabPane.setSelectedComponent(p);//temporarily switch to current tab
            JFileChooser fc = new JFileChooser();
            fc.showSaveDialog(null);
            p.file = fc.getSelectedFile();
            if (p.file == null) {
                return false;
            }
            Frame2.ths.tabPane.setSelectedIndex(selectedIndex);
        }
        if (textToFile(p.file, p.txt.getText())) {//write file
            return false;
        }
        p.modified = false;
        p.updateTab();
        return true;
    }

    public static boolean textToFile(File file, String text) {
        try {
            PrintStream out = new PrintStream(file);
            out.append(text);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(G.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
        return false;
    }

    public static boolean textToFile(String fname, String text) {
        try {
            PrintStream out = new PrintStream(fname);
            out.append(text);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(G.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
        return false;
    }

    public static void zzzsleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ex) {

        }
    }
    
public static boolean isValidName(String text)
{
    Pattern pattern = Pattern.compile(
        "# Match a valid Windows filename (unspecified file system).          \n" +
        "^                                # Anchor to start of string.        \n" +
        "(?!                              # Assert filename is not: CON, PRN, \n" +
        "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n" +
        "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n" +
        "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n" +
        "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n" +
        "  (?:\\.[^.]*)?                  # followed by optional extension    \n" +
        "  $                              # and end of string                 \n" +
        ")                                # End negative lookahead assertion. \n" +
        "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n" +
        "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n" +
        "$                                # Anchor to end of string.            ", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    Matcher matcher = pattern.matcher(text);
    boolean isMatch = matcher.matches();
    return isMatch;
}
}
