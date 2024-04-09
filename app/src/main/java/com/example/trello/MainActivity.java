package com.example.trello;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LinearLayout boardLayout;
    private ArrayList<Column> columns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLayout = findViewById(R.id.board_layout);

        addColumn("To Do");
        addColumn("In Progress");
        addColumn("Done");
    }

    private void addColumn(String columnName) {
        Column column = new Column(this, columnName);
        columns.add(column);
        boardLayout.addView(column.getView());
    }

    public void addCardToColumn(int columnIndex, Card card) {
        columns.get(columnIndex).addCard(card);
    }

    public void deleteColumn(int columnIndex) {
        boardLayout.removeViewAt(columnIndex);
        columns.remove(columnIndex);
    }

    public int getColumnIndexByName(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (column.getName().equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private class Column {
        private View view;
        private ArrayList<Card> cards = new ArrayList<>();
        private String name;

        public String getName() {
            return name;
        }
        public ArrayList<Card> getCards() {
            return cards;
        }

        public void removeCard(Card card, LinearLayout cardContainer) {
            cards.remove(card);

            for (int i = 0; i < cardContainer.getChildCount(); i++) {
                View view = cardContainer.getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (textView.getText().toString().equals(card.getTitle())) {
                        cardContainer.removeViewAt(i);
                        break;
                    }
                }
            }
        }

        public void updateCardUI(Card card, String oldTitle, LinearLayout cardContainer) {
            for (int i = 0; i < cardContainer.getChildCount(); i++) {
                View view = cardContainer.getChildAt(i);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (textView.getText().toString().equals(oldTitle)) {
                        textView.setText(card.getTitle());
                        break;
                    }
                }
            }
        }

        public Column(MainActivity activity, String columnName) {
            this.name = columnName;
            view = getLayoutInflater().inflate(R.layout.column_layout, null);
            TextView columnNameTextView = view.findViewById(R.id.column_name_textview);
            columnNameTextView.setText(columnName);
            view.findViewById(R.id.add_card_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddCardDialog(columns.indexOf(Column.this));
                }
            });
            view.findViewById(R.id.delete_column_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteColumn(columns.indexOf(Column.this));
                }
            });
        }

        public View getView() {
            return view;
        }

        public void addCard(Card card) {
            final Card finalCard = card;
            cards.add(finalCard);
            LinearLayout cardContainer = view.findViewById(R.id.card_container);

            TextView cardTextView = new TextView(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 20, 0, 20);
            cardTextView.setLayoutParams(layoutParams);
            cardTextView.setBackgroundResource(R.drawable.card_background); // Задаємо фон для карточки
            cardTextView.setText(card.getTitle());
            cardTextView.setPadding(20, 20, 20, 20);
            cardTextView.setTextSize(16);

            cardTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditCardDialog(columns.indexOf(Column.this), finalCard, cardContainer);
                }
            });

            cardContainer.addView(cardTextView);
        }
    }

    public void showAddCardDialog(Integer columnId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.card_add_dialog, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.title_edittext);
        EditText descriptionEditText = dialogView.findViewById(R.id.description_edittext);
        EditText expirationEditText = dialogView.findViewById(R.id.expiration_edittext);
        EditText assigneeEditText = dialogView.findViewById(R.id.assignee_edittext);
        EditText authorEditText = dialogView.findViewById(R.id.author_edittext);
        EditText tagsEditTet = dialogView.findViewById(R.id.tags_edittext);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        AlertDialog alertDialog = builder.create();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String expirationDate = expirationEditText.getText().toString().trim();
                String assignee = assigneeEditText.getText().toString().trim();
                String author = authorEditText.getText().toString().trim();
                String tags = tagsEditTet.getText().toString().trim();

                Card newCard = new Card(title, columns.get(columnId).getName(), description, expirationDate, assignee, author, tags);
                addCardToColumn(columnId, newCard);

                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void showEditCardDialog(Integer columnIndex, Card card, LinearLayout cardContainer) {
        Column oldColumn = columns.get(columnIndex);
        String oldStatus = card.getStatus();
        String oldTitle = card.getTitle();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.card_edit_dialog, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.title_edittext);
        Spinner statusSpinner = dialogView.findViewById(R.id.status_spinner);
        EditText descriptionEditText = dialogView.findViewById(R.id.description_edittext);
        EditText expirationEditText = dialogView.findViewById(R.id.expiration_edittext);
        EditText assigneeEditText = dialogView.findViewById(R.id.assignee_edittext);
        EditText authorEditText = dialogView.findViewById(R.id.author_edittext);
        EditText tagsEditTet = dialogView.findViewById(R.id.tags_edittext);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        titleEditText.setText(card.getTitle());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        int position = adapter.getPosition(card.getStatus());
        statusSpinner.setSelection(position);
        descriptionEditText.setText(card.getDescription());
        expirationEditText.setText(card.getExpirationDate());
        assigneeEditText.setText(card.getAssigneeUser());
        authorEditText.setText(card.getAuthorUser());
        tagsEditTet.setText(card.getTags());

        AlertDialog alertDialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleEditText.getText().toString().trim();
                String newStatus = statusSpinner.getSelectedItem().toString();
                String newDescription = descriptionEditText.getText().toString().trim();
                String expirationDate = expirationEditText.getText().toString().trim();
                String assignee = assigneeEditText.getText().toString().trim();
                String author = authorEditText.getText().toString().trim();
                String tags = tagsEditTet.getText().toString().trim();

                card.setTitle(newTitle);
                card.setStatus(newStatus);
                card.setDescription(newDescription);
                card.setExpirationDate(expirationDate);
                card.setAssigneeUser(assignee);
                card.setAuthorUser(author);
                card.setTags(tags);

                Column newColumn = columns.get(getColumnIndexByName(newStatus));


                if (!Objects.equals(oldStatus, newStatus)) {
                    oldColumn.removeCard(card, cardContainer);

                    newColumn.addCard(card);
                }

                newColumn.updateCardUI(card, oldTitle, cardContainer);

                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}