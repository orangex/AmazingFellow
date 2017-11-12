package com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.features.homepage.recent.RecentDataViewHolder;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.utils.DotaUtil;
import com.orangex.amazingfellow.utils.TypefaceUtil;

import butterknife.BindView;

/**
 * Created by orangex on 2017/11/3.
 */

public class RecentDotaDataViewHolder extends RecentDataViewHolder {
    @BindView(R.id.bg)
    ImageView mBackgound;
    
    @BindView(R.id.tv_player_name)
    TextView mTvPlayername;
    @BindView(R.id.tv_last)
    TextView mTvLast;
    @BindView(R.id.tv_mvp)
    TextView mTvMVP;
    @BindView(R.id.tv_glory_kill)
    TextView mTvKill;
    @BindView(R.id.tv_glory_destroy)
    TextView mTvDestroy;
    @BindView(R.id.tv_glory_gold)
    TextView mTvGold;
    @BindView(R.id.tv_glory_health)
    TextView mTvHealth;
    @BindView(R.id.tv_glory_assist)
    TextView mTvAssist;
    @BindView(R.id.tv_kda)
    TextView mTvKda;
    @BindView(R.id.tv_efficiency)
    TextView mTvEfficiency;
    @BindView(R.id.tv_gpm)
    TextView mTvGpm;
    @BindView(R.id.tv_dhb)
    TextView mTvDhb;
    @BindView(R.id.tv_hero_name)
    TextView mTvHeroName;
    @BindView(R.id.tv_timeoffsetdesc)
    TextView mTvTimeoffsetdesc;
    @BindView(R.id.tv_game_level)
    TextView mTVGamelevel;
    private static final String TAG ="datui "+ RecentDotaDataViewHolder.class.getSimpleName();
    
    public RecentDotaDataViewHolder(Context context, ViewGroup parent) {
        super(context,parent, R.layout.item_recent_dota);
    }
    
    @Override
    public void setData(MatchModel matchModel) {
        DotaMatchModel dotaDataModel = (DotaMatchModel) matchModel;
        Glide.with(itemView.getContext())
                .load(DotaUtil.getHeroPicById(dotaDataModel.getHero()))
                .into(mBackgound);
        mTvHeroName.setText(DotaUtil.getHeroLocNameById(dotaDataModel.getHero()));
        mTvLast.setText(dotaDataModel.getLastTime() + "分钟");
        if (dotaDataModel.getMvpType() == DotaMatchModel.MVP_TYPE_GLORIOUS) {
            mTvMVP.setText("虽败犹荣");
            mTvMVP.setVisibility(View.VISIBLE);
        } else {
            mTvMVP.setVisibility(View.GONE);
        }
        if (dotaDataModel.getGlorys().contains(DotaMatchModel.GLORY_KILL)) {
            mTvKill.setVisibility(View.VISIBLE);
        } else {
            mTvKill.setVisibility(View.GONE);
        }
    
        if (dotaDataModel.getGlorys().contains(DotaMatchModel.GLORY_DESTROY)) {
            mTvDestroy.setVisibility(View.VISIBLE);
        } else {
            mTvDestroy.setVisibility(View.GONE);
        }
    
        if (dotaDataModel.getGlorys().contains(DotaMatchModel.GLORY_GOLD)) {
            mTvGold.setVisibility(View.VISIBLE);
        } else {
            mTvGold.setVisibility(View.GONE);
        }
        if (dotaDataModel.getGlorys().contains(DotaMatchModel.GLORY_HEALTH)) {
            mTvHealth.setVisibility(View.VISIBLE);
        } else {
            mTvHealth.setVisibility(View.GONE);
        }
        if (dotaDataModel.getGlorys().contains(DotaMatchModel.GLORY_ASSIST)) {
            mTvAssist.setVisibility(View.VISIBLE);
        } else {
            mTvAssist.setVisibility(View.GONE);
        }
        mTvKda.setText(dotaDataModel.getKills() + "/" + dotaDataModel.getDeaths() + "/" + dotaDataModel.getAssits());
        mTvEfficiency.setText(dotaDataModel.getEfficiency());
        mTvGpm.setText(String.valueOf(dotaDataModel.getGpm()));
        mTvDhb.setText(String.valueOf(dotaDataModel.getDhb()));
        mTvPlayername.setText(dotaDataModel.getPlayerName());
        mTvTimeoffsetdesc.setText("于" + dotaDataModel.getTimeOffsetDesc());
    
        TypefaceUtil.TYPEFACE.setTypefaceWawa(mTVGamelevel);
        if (dotaDataModel.getGameLevel() == DotaMatchModel.GAME_LEVEL_HIGH) {
            mTVGamelevel.setTextColor(AFApplication.getAppContext().getResources().getColor(R.color.tv_gamelevel_high));
            mTVGamelevel.setText("High");
        } else if (dotaDataModel.getGameLevel() == DotaMatchModel.GAME_LEVEL_VERYHIGN) {
            mTVGamelevel.setTextColor(AFApplication.getAppContext().getResources().getColor(R.color.tv_gamelevel_veryhigh));
            mTVGamelevel.setText("Very High");
        }
        
        //Log.i(TAG, "setData: " + dotaDataModel.toString());
    }
}
