package org.delta.pass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 19-02-2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private String[] mDataset;
    ArrayList<String> contact;
    ArrayList<String> timestamp;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Contact;
        public TextView TimeStamp;
        public ViewHolder(View v) {
            super(v);
            Contact = (TextView)v.findViewById(R.id.Contact);
            TimeStamp = (TextView)v.findViewById(R.id.Time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatListAdapter(ArrayList<String> contact,ArrayList<String> tm) {
        this.contact=contact;
        this.timestamp=tm;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatlist_single, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Contact.setText(contact.get(position));
        holder.TimeStamp.setText(timestamp.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contact.size();
    }
}
