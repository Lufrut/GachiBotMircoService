package org.gachi.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    public void commandSorter(Update update);
}
