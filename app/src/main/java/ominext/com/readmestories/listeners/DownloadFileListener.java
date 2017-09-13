package ominext.com.readmestories.listeners;

/**
 * Created by LuongHH on 6/29/2017.
 */

public interface DownloadFileListener {

    void onDownloadSuccessful(String filePath);

    void onDownloadFailed();
}
