package org.gachi.configuration;

import org.gachi.enums.LanguageCodes;

public class Locales {
    public static String getHelpLocale(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return "there is help";
            }
            case UA -> {
                return "Тут допомога";
            }
            case RU -> {
                return "Тут помощь";
            }
            default -> {
                return "problem with locales";
            }

        }
    }

    public static String registerLocale(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return "You have successfully registered";
            }
            case UA -> {
                return "Ви успішно зареєструвалися";
            }
            case RU -> {
                return "Вы успешно зарегестрировались";
            }
            default -> {
                return "Problem with registration locales";
            }
        }
    }

    public static String alreadyRegistered(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return "You have already registered";
            }
            case UA -> {
                return "Ви вже зареєстровані";
            }
            case RU -> {
                return "Вы уже зарегестрированны";
            }
            default -> {
                return "Problem with alreadyRegistered locales";
            }
        }
    }

    public static String drunkCumSuccessfully(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return "You have successfully drink cum ";
            }
            case UA -> {
                return "Ви успішно випили кам ";
            }
            case RU -> {
                return "Вы успешно выпили кам ";
            }
            default -> {
                return "Problem with drunkCumSuccessfully locale";
            }
        }
    }

    public static String drunkCumCooldown(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return "You have already drunk cum please wait ";
            }
            case UA -> {
                return "Ви вже випили кам зачекайте ";
            }
            case RU -> {
                return "Вы уже выпили кам подождите ";
            }
            default -> {
                return "Problem with drunkCumCooldown locale";
            }
        }
    }

    public static String minutes(LanguageCodes code) {
        switch (code) {
            case EN -> {
                return " minutes";
            }
            case UA -> {
                return " хвилин";
            }
            case RU -> {
                return " минут";
            }
            default -> {
                return " Problem with minutes locale";
            }
        }
    }
    public static String gotSlavesSuccessfully(LanguageCodes code){
        switch (code){
            case EN -> {
                return "You successfully got slaves: ";
            }
            case UA -> {
                return "Ви успішно знайшли рабів: ";
            }
            case RU -> {
                return "Вы успешно нашли рабов: ";
            }
            default -> {
                return " Problem with gotSlavesSuccessfully locale";
            }
        }
    }

    public static String gotSlavesCooldown(LanguageCodes code){
        switch (code){
            case EN -> {
                return "You have already got slaves please wait ";
            }
            case UA -> {
                return "Ви вже отримали рабів зачекайте ";
            }
            case RU -> {
                return "Вы уже получили рабов подождите ";
            }
            default -> {
                return " Problem with gotSlavesCooldown locale";
            }
        }
    }
}
