package com.example.recipebook_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    private Context context;
    private ArrayList<Model> arrayList;
    //db object
    DatabaseHelper databaseHelper;

    public Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Model model = arrayList.get(position);
        //get for view
        String id = model.getId();
        String title = model.getTitle();
        String image = model.getImage();
        String ingredients = model.getIngredients();
        String steps = model.getSteps();
        final String addTimeStamp = model.getAddTimeStamp();
        final String updateTimeStamp = model.getUpdateTimeStamp();

        //set view
        holder.foodTitle.setText(title);
        holder.foodImage.setImageURI(Uri.parse(image));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(
                        ""+position,
                        ""+id,
                        ""+title,
                        ""+ingredients,
                        ""+steps,
                        ""+image,
                        ""+addTimeStamp,
                        ""+updateTimeStamp
                );
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(
                        ""+id
                );
            }
        });
    }

    private void deleteDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this recipe?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_delete_24);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteInfo(id);
                ((MainActivity)context).onResume();
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void editDialog(String position, String id, String title, String ingredients, String steps, String image, String addTimeStamp, String updateTimeStamp) {
        Intent intent = new Intent(context, EditRecordActivity.class);
        intent.putExtra("ID", id);
        intent.putExtra("TITLE", title);
        intent.putExtra("IMAGE", image);
        intent.putExtra("INGREDIENTS", ingredients);
        intent.putExtra("STEPS", steps);
        intent.putExtra("ADD_TIMESTAMP", addTimeStamp);
        intent.putExtra("UPDATE_TIMESTAMP", updateTimeStamp);
        intent.putExtra("editMode", true);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodTitle;
        CardView editButton;
        ImageButton deleteButton;
        TextView foodIngredients;
        TextView foodSteps;

        public Holder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_single_view);
            foodTitle = itemView.findViewById(R.id.textView_single_view);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
