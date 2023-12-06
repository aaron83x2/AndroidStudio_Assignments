package com.example.aaronsandroidlab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.aaronsandroidlab.databinding.ActivityChatRoomBinding;
import com.example.aaronsandroidlab.databinding.RecieveMessageBinding;
import com.example.aaronsandroidlab.databinding.SendMessageBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatRoomViewModel  chatModel;

    ChatMessageDAO mDAO;


    private RecyclerView.Adapter myAdapter = null;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int selection = item.getItemId();


        if(selection == R.id.delete){
            Toast.makeText(this, "Message Deleted", Toast.LENGTH_SHORT).show();

            // Get the selected message from the ViewModel
            ChatMessage selectedMessage = chatModel.selectedMessage.getValue();

            // Check if a message is selected
            if (selectedMessage != null) {

                new AlertDialog.Builder(ChatRoom.this)
                        .setMessage("Do you want to delete the message " + selectedMessage.getMessage())
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, which) -> {})
                        .setPositiveButton("Yes", (dialog, which) -> {

                            // Delete the message from the list and notify the adapter
                            int position = messages.indexOf(selectedMessage);


                            // Delete the message from the database
                            Executor thread2 = Executors.newSingleThreadExecutor();

                            thread2.execute(() -> {
                                mDAO.deleteMessages(selectedMessage);


                            });
                            messages.remove(selectedMessage);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(binding.getRoot(), "You deleted the message", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", view -> {

                                        Executor thread = Executors.newSingleThreadExecutor();


                                        thread.execute(() ->{
                                            mDAO.insertMessage(selectedMessage);
                                        });

                                        messages.add(position, selectedMessage);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show();

            } else {
                Toast.makeText(this, "No Message Selected", Toast.LENGTH_SHORT).show();
            }
        }


        if(selection == R.id.about){
            Toast.makeText(this, "Version 1.0, created by Aaron Odartei", Toast.LENGTH_SHORT).show();
        }


                //put your ChatMessage deletion code here. If you select this item, you should show the alert dialog
                //asking if the user wants to delete this message.
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Create Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);





        //Retrieve the arrayList<>
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        messages = chatModel.messages.getValue();

        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<>());

            //Build Database
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "ChatMessage").build();
            mDAO = db.cmDAO();

            //Get All Entries from database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {

                List<ChatMessage> fromDatabase = mDAO.getAllMessages();//return a List
                messages.addAll(fromDatabase);//this adds all messages from the database

            });
        }

        //Create Details Fragment
        chatModel.selectedMessage.observe(this, (newMsgValue) -> {


            MessageDetailsFragment chatFragment = new MessageDetailsFragment( newMsgValue );


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLocation, chatFragment)
                    .addToBackStack("")
                    .commit();

        });

        //If Sender is clicked
        binding.sendButton.setOnClickListener(click -> {
            //Get Text from input
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");//Get Date in time in Simple Format
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatmsg = new ChatMessage(msg, currentDateAndTime, true);//New ChatMessage Object
            messages.add(chatmsg);//Add new Chat Object to the ArrayList
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");//Clear what was typed

            //Tell Recycler view to update
            myAdapter.notifyDataSetChanged();

            //Add to database on another thread:
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(( ) -> {
                //this is on a background thread
                chatmsg.id = mDAO.insertMessage(chatmsg); //get the ID from the database

            });

        });

        //If Receiver is Clicked
        binding.recieveButton.setOnClickListener(click -> {
            //Get Text from input
            String msg = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatmsg = new ChatMessage(msg, currentDateandTime, false);//New ChatMessage Object
            messages.add(chatmsg);//Add new Chat Object to the ArrayList

            //Clear previous text
            binding.textInput.setText("");

            myAdapter.notifyDataSetChanged();

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(( ) -> {
                //this is on a background thread
                chatmsg.id = mDAO.insertMessage(chatmsg); //get the ID from the database

            });

        });


        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if (viewType == 0) {
                    SendMessageBinding binding = SendMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());

                } else {
                    RecieveMessageBinding binding = RecieveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                ChatMessage cht = messages.get(position);

                holder.messageText.setText(cht.getMessage());
                holder.timeText.setText(cht.getTimeSent());

            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage cht = messages.get(position);

                if (cht.isSentButton) {
                    return 0;
                } else {
                    return 1;
                }
            }

        });

        //Initialize the chat room model object
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));



    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {

                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);


//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//                builder.setMessage("Do you want to delete the message " + messageText.getText())
//                        .setTitle("Question:")
//                        .setNegativeButton("No", (Dialog, Click) -> {})
//                        .setPositiveButton("Yes", (Dialog, Click) ->{
//
//                            ChatMessage removedMessage = messages.get(position);
//
//                                Executor thread2 = Executors.newSingleThreadExecutor();
//                                        thread2.execute(() -> {
//                                        //delete from database
//                                        mDAO.deleteMessages(removedMessage);
//                                });
//
//                                messages.remove(position);
//                                myAdapter.notifyItemRemoved(position);
//
//
//                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", clk ->{
//                                        messages.add(position, removedMessage);
//                                        myAdapter.notifyItemRemoved(position);
//                                    })
//                                    .show();
//
//                    }).create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

}
