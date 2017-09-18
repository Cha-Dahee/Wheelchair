package org.milal.wheeliric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ekgml on 2017-08-14.
 */

public class ReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<ReviewItems> reviewItemsArrayList;

    TextView nick;
    TextView date;
    TextView grade;
    TextView comment;
    ImageView photo_re;
    ImageView toilet_re;
    ImageView entrance_re;
    ImageView park_re;
    ImageView table_re;
    ImageView like;
    TextView like_num;

    public ReviewAdapter(Context context, ArrayList<ReviewItems> reviewItemsArrayList) {
        this.context = context;
        this.reviewItemsArrayList = reviewItemsArrayList;
    }

    @Override
    //해당 장소에 대한 리뷰 개수 보여줍니다.
    public int getCount() {
        return this.reviewItemsArrayList.size();
    }

    @Override
    //해당 장소에서 몇번째 리뷰를 보는지 알려줍니다.
    public Object getItem(int position) {
        return this.reviewItemsArrayList.get(position);
    }

    @Override
    //현재 어떤 포지션인지 알려줍니다.
    public long getItemId(int position) {
        return position;
    }

    @Override
    //리스트뷰에서 아이템과 xml을 연결하여 화면에 표시해줍니다.
    //반복문을 통해 한칸씩 화면을 구성합니다.
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.review_items, null);
        }

        nick = (TextView) convertView.findViewById(R.id.nick);
        date = (TextView) convertView.findViewById(R.id.date);
        grade = (TextView) convertView.findViewById(R.id.grade);
        comment = (TextView) convertView.findViewById(R.id.comment);
        photo_re = (ImageView) convertView.findViewById(R.id.photo_re);
        toilet_re = (ImageView) convertView.findViewById(R.id.toilet_re);
        park_re = (ImageView) convertView.findViewById(R.id.park_re);
        table_re = (ImageView) convertView.findViewById(R.id.table_re);
        entrance_re = (ImageView) convertView.findViewById(R.id.entrance_re);
        like = (ImageView) convertView.findViewById(R.id.like);
        like_num = (TextView) convertView.findViewById(R.id.like_num);

        nick.setText(reviewItemsArrayList.get(position).getNick());
        date.setText(reviewItemsArrayList.get(position).getDate());
        grade.setText(reviewItemsArrayList.get(position).getGrade());
        comment.setText(reviewItemsArrayList.get(position).getComment());

        photo_re.setImageResource(reviewItemsArrayList.get(position).getPhotoURL());

        toilet_re.setImageResource(reviewItemsArrayList.get(position).getToilet());

        park_re.setImageResource(reviewItemsArrayList.get(position).getPark());
        table_re.setImageResource(reviewItemsArrayList.get(position).getTable());
        entrance_re.setImageResource(reviewItemsArrayList.get(position).getEntrance());
        like.setImageResource(reviewItemsArrayList.get(position).getLike());
        like_num.setText(String.valueOf(reviewItemsArrayList.get(position).getLikeNum()));

        return convertView;
    }
}
