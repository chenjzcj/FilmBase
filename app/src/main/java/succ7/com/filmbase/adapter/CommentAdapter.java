package succ7.com.filmbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Comment;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 评论列表适配器
 */
public class CommentAdapter extends BaseAdapter {
    private List<Comment> comments;
    private Context context;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments != null ? comments.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = comments.get(position);
        viewHolder.tv_critic.setText(comment.getCritic().getUsername());
        viewHolder.tv_content.setText(comment.getContent());
        return convertView;
    }

    class ViewHolder {
        TextView tv_critic;
        TextView tv_content;

        public ViewHolder(View root) {
            this.tv_critic = (TextView) root.findViewById(R.id.tv_critic);
            this.tv_content = (TextView) root.findViewById(R.id.tv_content);
        }
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.notifyDataSetChanged();
    }
}
