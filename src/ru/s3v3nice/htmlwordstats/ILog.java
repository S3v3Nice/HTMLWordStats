package ru.s3v3nice.htmlwordstats;

public interface ILog {
    /**
     * Логирует ошибку в консоль и лог-файл
     *
     * @param msg сообщение об ошибке
     */
    void error(String msg);
}
