package wifi.svdew.myapplication.ui.news;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import wifi.svdew.myapplication.R;

public class NewsRecycleAdapter extends RecyclerView.Adapter<NewsRecycleAdapter.NewsViewHolder> {

    private List<Article> articleList;

    public NewsRecycleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycle_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());

        // Kép betöltés biztonságosan
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Picasso.get()
                    .load(article.getUrlToImage())
                    .resize(600, 400)         // max méret, hogy ne legyen túl nagy bitmap
                    .centerCrop()             // hogy szépen illeszkedjen
                    .error(R.drawable.baseline_newspaper_24)
                    .placeholder(R.drawable.baseline_newspaper_24)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.baseline_newspaper_24);
        }

        // Teljes hír megnyitása kattintásra
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewsFullActivity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    public void updateData(List<Article> data) {
        articleList.clear();
        articleList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, sourceTextView;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
        }
    }
}