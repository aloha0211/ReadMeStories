package ominext.com.readmestories.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import ominext.com.readmestories.R;
import ominext.com.readmestories.activities.BaseActivity;
import ominext.com.readmestories.activities.CategoryBooksActivity;
import ominext.com.readmestories.listeners.DownloadFileListener;
import ominext.com.readmestories.models.Book;
import ominext.com.readmestories.models.BookResponse;
import ominext.com.readmestories.models.Category;
import ominext.com.readmestories.models.GlideApp;
import ominext.com.readmestories.utils.Constant;
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
        final CategoryViewHolder holder = new CategoryViewHolder(itemView);
        itemView.setOnClickListener(view -> {
            Category category = mCategories.get(holder.getAdapterPosition());
            onCategoryClick(category);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        holder.tvCategoryName.setText(category.getTitle());
        GlideApp.with(mContext)
                .load(category.getLargeTout())
                .placeholder(R.color.light_azure)
                .into(holder.ivCategory);
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    private void onCategoryClick(Category category) {
        ((BaseActivity) mContext).showProgressDialog(mContext.getString(R.string.loading_data));
        mSelectedCategory = category;

        List<Book> localBooks = new ArrayList<>();
        final List<Integer> bookIds = new ArrayList<>();
        localBooks.addAll(Utils.getBooksFromAssets(mContext));
        localBooks.addAll(Utils.getBooksFromRealm((BaseActivity) mContext));
        Stream.of(localBooks).forEach(book -> bookIds.add(book.getId()));
        List<BookResponse> bookList =  Stream.of(mSelectedCategory.getBooks()).filter(book -> !bookIds.contains(book.getId())).toList();
        mSelectedCategory.setBooks(bookList);

        mFileDownloadedIndex = 0;
        mTotalFile = category.getBooks().size();
        if (mTotalFile == 0) {
            startCategoryBooksActivity();
        } else {
            String refPath = mSelectedCategory.getBooks().get(0).getId() + "/" + Constant.IMAGE;
            String storePath = Constant.CATEGORY + "/" + refPath;
            Utils.downloadToCacheFolder(mContext, refPath, storePath, Constant.COVER, mListener);
        }
    }

    private int mFileDownloadedIndex;
    private int mTotalFile;
    private Category mSelectedCategory;

    private DownloadFileListener mListener = new DownloadFileListener() {
        @Override
        public void onDownloadSuccessful(String audioPath) {
            mFileDownloadedIndex++;
            if (mFileDownloadedIndex < mTotalFile) {
                String refPath = mSelectedCategory.getBooks().get(mFileDownloadedIndex).getId() + "/" + Constant.IMAGE;
                String storePath = Constant.CATEGORY + "/" + refPath;
                Utils.downloadToCacheFolder(mContext, refPath, storePath, Constant.COVER, mListener);
            } else if (mFileDownloadedIndex == mTotalFile) {
                // downloadToCacheFolder all cover image files for all books finished
                startCategoryBooksActivity();
            }
        }

        @Override
        public void onDownloadFailed() {
            ((BaseActivity) mContext).dismissProgressDialog();
            ((BaseActivity) mContext).showAlertDialog(mContext.getString(R.string.error), mContext.getString(R.string.load_data_err_msg));
            Utils.deleteCacheDir(mContext, Constant.STORY + "/" + Constant.CATEGORY);
        }
    };

    private void startCategoryBooksActivity() {
        Intent intent = new Intent(mContext, CategoryBooksActivity.class);
        ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < mSelectedCategory.getBooks().size(); i++) {
            BookResponse response = mSelectedCategory.getBooks().get(i);
            Book book = new Book();
            book.setId(response.getId());
            book.setTitle(response.getTitle());
            book.setAuthor(response.getAuthor());
            book.setIllustrator(response.getIllustrator());
            book.setReadingMode(Constant.MODE_FROM_CACHE);
            books.add(book);
        }
        intent.putExtra(Constant.KEY_TITLE, mSelectedCategory.getTitle());
        intent.putParcelableArrayListExtra(Constant.KEY_BOOKS, books);
        mContext.startActivity(intent);
        ((BaseActivity) mContext).dismissProgressDialog();
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
