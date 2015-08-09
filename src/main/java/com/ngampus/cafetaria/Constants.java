package com.ngampus.cafetaria;

import android.provider.BaseColumns;

/**
 * Created by Faizal Lutviyanto on 7/30/2015.
 */
/*interface Constants turunan dari BaseColumn
*public interface BaseColumns {
    String _ID = "_id";
    String _COUNT = "_count";
}
*String _ID ="_id" digunakan untuk primary key dalam database
*
*interface Constants
* */
public interface Constants extends BaseColumns {
    String TABLE_NAME   = "tabelminuman";

    String UKURAN_GL    = "ukuranGL";
    String PILIHAN_HD   = "pilihanHD";
    String RASA_PILIHAN = "rasaPilihan";

}
