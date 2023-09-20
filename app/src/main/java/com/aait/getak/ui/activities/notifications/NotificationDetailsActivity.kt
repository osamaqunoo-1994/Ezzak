package com.aait.getak.ui.activities.notifications

import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.notification_model.Data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_notification_details.*

class NotificationDetailsActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_notification_details

    override fun init() {
        var data=intent.getSerializableExtra("trip") as (Data)
        Picasso.get().load(data.img).into(img_disp)
        title_tv.text = data.title
        details.text = data.desc
        date.text = data.date

    }


}
