/*
 * Copyright 2013 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rmtheis.yandtran.language;

/**
 * Language - an enum of language codes supported by the Yandex API
 */
public enum Language {
  AFRIKAANS("af"),
  ALBANIAN("sq"),
  AMHARIC("am"),
  ARABIC("ar"),
  ARMENIAN("hy"),
  AZERBAIJANI("az"),
  BASQUE("eu"),
  BASHKIR("ba"),
  BELARUSIAN("be"),
  BENGALI("bn"),
  BULGARIAN("bg"),
  BOSNIAN("bs"),
  CATALAN("ca"),
  CHINESE("zh"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  ESTONIAN("et"),
  ESPERANTO("eo"),
  FINNISH("fi"),
  FRENCH("fr"),
  GALICIAN("gl"),
  GERMAN("de"),
  GEORGIAN("ka"),
  GREEK("el"),
  GUJARATI("gu"),
  HUNGARIAN("hu"),
  HAITIAN("ht"),
  HILL_MARI("mrj"),
  HEBREW("he"),
  HINDI("hi"),
  INDONESIAN("id"),
  ITALIAN("it"),
  IRISH("ga"),
  ICELANDIC("is"),
  JAPANESE("ja"),
  JAVANESE("jv"),
  KAZAKH("kk"),
  KANNADA("kn"),
  KYRGYZ("ky"),
  KOREAN("ko"),
  LATVIAN("lv"),
  LATIN("la"),
  LITHUANIAN("lt"),
  LUXEMBOURGISH("lb"),
  MALAGASY("mg"),
  MALAY("ms"),
  MALAYALAM("ms"),
  MACEDONIAN("mk"),
  MALTESE("mt"),
  MAORI("mi"),
  MARATHI("mr"),
  MARI("mhr"),
  MONGOLIAN("mn"),
  NORWEGIAN("no"),
  NEPALI("ne"),
  PUNJABI("pa"),
  PAPIAMENTO("pap"),
  PERSIAN("fa"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SERBIAN("sr"),
  SINHALA("si"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SWAHILI("sw"),
  SUNDANESE("su"),
  SPANISH("es"),
  SWEDISH("sv"),
  SCOTTISH("gd"),
  TAJIK("tg"),
  THAI("th"),
  TAGALOG("tl"),
  TAMIL("ta"),
  TATAR("tt"),
  TELUGU("te"),
  TURKISH("tr"),
  UKRAINIAN("uk"),
  UDMURT("udm"),
  UZBEK("uz"),
  URDU("ur"),
  VIETNAMESE("vi"),
  WELSH("cy"),
  XHOSA("xh"),
  YIDDISH("yi");

  /**
   * String representation of this language.
   */
  private final String language;

  /**
   * Enum constructor.
   * @param pLanguage The language identifier.
   */
  private Language(final String pLanguage) {
    language = pLanguage;
  }

  public static Language fromString(final String pLanguage) {
    for (Language l : values()) {
      if (l.toString().equals(pLanguage)) {
        return l;
      }
    }
    return null;
  }

  public static boolean contains(String test) {

    for (Language c : Language.values()) {
      if (c.name().equalsIgnoreCase(test)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the String representation of this language.
   * @return The String representation of this language.
   */
  @Override
  public String toString() {
    return language;
  }

}
