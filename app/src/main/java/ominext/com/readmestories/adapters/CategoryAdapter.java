package ominext.com.readmestories.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.models.Category;
import ominext.com.readmestories.models.GlideApp;
import ominext.com.readmestories.utils.Utils;

/**
 * Created by LuongHH on 6/21/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<Category> mCategories;

    public CategoryAdapter(Context context, List<Category> list) {
        this.mContext = context;
        this.mCategories = list;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        holder.tvCategoryName.setText(category.getTitle());
        GlideApp.with(mContext)
                .load(category.getUri())
                .placeholder(R.drawable.background)
                .into(holder.ivCategory);
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCategory;
        TextView tvCategoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ivCategory = (ImageView) itemView.findViewById(R.id.iv_category);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
        }
    }
}
