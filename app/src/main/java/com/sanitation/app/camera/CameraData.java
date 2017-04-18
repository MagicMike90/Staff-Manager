package com.sanitation.app.camera;

import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.Size;

import java.util.List;

/**
 * Created by Michael on 4/18/17.
 */


public interface CameraData {
    List<FileData> getFileDatas();

    List<String> getFilePaths();

    Size getSize();
}
