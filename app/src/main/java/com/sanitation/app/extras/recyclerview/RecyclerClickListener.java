package com.sanitation.app.extras.recyclerview;

import android.view.View;

/**
 * Created by Michael on 2/12/17.
 */

public interface RecyclerClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
