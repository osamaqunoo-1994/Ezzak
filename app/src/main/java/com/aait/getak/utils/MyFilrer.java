package com.aait.getak.utils;

import android.widget.Filter;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

/**
 * Created by human on 10/11/17.
 */

public class MyFilrer extends Filter {

    public void logoutFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            // already logged out
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> LoginManager.getInstance().logOut()).executeAsync();
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }

   /* private GeneralAdvAdapter adapter;
    private List<DataBean> allAdvs;
    private List<DataBean> filtered_advs;

    public MyFilrer(GeneralAdvAdapter adapter, List<DataBean> allAdvs) {
        this.adapter = adapter;
        this.allAdvs = allAdvs;
        this.filtered_advs = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        filtered_advs.clear();
        FilterResults results = new FilterResults();
        if (charSequence.length()==0){
            filtered_advs.addAll(allAdvs);

        }
        else {
            String s = charSequence.toString().toLowerCase().trim();
            for (DataBean adv:
                 allAdvs) {
                if (adv.getTitle().contains(s)||
                        adv.getAddress().contains(s)||
                        adv.getArea().contains(s)||
                        adv.getCountry().contains(s)||
                        adv.getLat().contains(s)||
                        adv.getLng().contains(s)||
                        adv.getDesc().equalsIgnoreCase(s)){
                    filtered_advs.add(adv);
                }
            }
            results.values=filtered_advs;
            results.count=filtered_advs.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        filtered_advs.clear();
        filtered_advs.addAll((List<DataBean>) filterResults.values);
        adapter.notifyDataSetChanged();
    }*/
}
