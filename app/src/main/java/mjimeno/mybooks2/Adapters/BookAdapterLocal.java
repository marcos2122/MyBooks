package mjimeno.mybooks2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

public class BookAdapterLocal extends RecyclerView.Adapter<BookAdapterLocal.BookVH> {
    Context context;
    List<Book.BookItem> libros;

    public BookAdapterLocal(Context context,List<Book.BookItem> libros){
        this.context= context;
        this.libros=libros;
    }


    @NonNull
    @Override
    public BookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_impar,parent,false);
       BookVH viewHolder = new BookVH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookVH holder, int position) {

        holder.mIdView.setText(String.valueOf(libros.get(position).identificador));
        holder.mTitulo.setText(libros.get(position).title);
        holder.mAutor.setText(libros.get(position).author);
    }



    @Override
    public int getItemCount() {
        return  libros.size();
    }
    class BookVH extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mTitulo;
        final TextView mAutor;

        public BookVH(View itemView) {
            super(itemView);

            mIdView = (TextView) itemView.findViewById(R.id.textViewIdentificador);
            mTitulo = (TextView) itemView.findViewById(R.id.textViewTitulo);
            mAutor =(TextView) itemView.findViewById(R.id.textViewAutor);



        }
    }
}
