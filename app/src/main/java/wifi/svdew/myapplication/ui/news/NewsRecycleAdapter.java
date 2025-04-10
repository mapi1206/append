package wifi.svdew.myapplication.ui.news;

// Adapter for displaying news articles in a RecyclerView using a custom layout
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

    // List of news articles to display
    private List<Article> articleList;

    // Constructor to initialize the article list
    public NewsRecycleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    // Inflate the custom row layout for each article item
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycle_row, parent, false);
        return new NewsViewHolder(view);
    }

    // Bind the article data (title, source, image) to the view holder
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());

        // Load image using Picasso if URL is valid
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

        // Set click listener to open full article in WebView activity
        // Teljes hír megnyitása kattintásra
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewsFullActivity.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    // Update the list of articles and refresh the RecyclerView
    public void updateData(List<Article> data) {
        articleList.clear();
        articleList.addAll(data);
        notifyDataSetChanged();
    }

    // Return the total number of articles
    @Override
    public int getItemCount() {
        return articleList.size();
    }

    // ViewHolder to hold references to article views (title, source, image)
    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, sourceTextView;
        ImageView imageView;

        // Initialize views from the layout
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
        }
    }
}