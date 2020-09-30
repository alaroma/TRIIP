package com.example.triip;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Toast;

public class Dialogs {
    public static final int IDD_MAIN = 1;
    public static final int IDD_DEL = 2;
    public static final int IDD_ABOUT = 3; // Идентификаторы диалоговых окон

    public static AlertDialog getDialog(final Activity activity, int ID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        switch(ID) {
            case IDD_ABOUT: // Диалоговое окно О приложении (настройки)

                LayoutInflater inflater = activity.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog, null));
                builder.setTitle("О приложениии");
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                return builder.create();
            case IDD_MAIN: // Диалоговое окно Редактировать/Удалить
                    final String[] ed_lab ={"Редактировать", "Удалить"};
                    builder.setItems(ed_lab, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch(item) {
                                case 0: // Открытие странцы редактирования метки
                                    Toast.makeText(activity, "В разработке", Toast.LENGTH_LONG).show();
                                    break;
                                case 1: // Удаление метки
                                    Dialog delete = Dialogs.getDialog(activity, Dialogs.IDD_DEL);
                                    dialog.dismiss();
                                    delete.show();
                                    break;
                            }
                        }
                    });
                    builder.setCancelable(true);
                    return builder.create();

            case IDD_DEL: // Диалог удаления метки
               builder.setMessage("Удалить метку безвозвратно?");
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { // Кнопка ОК
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(activity, "В разработке", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка Отмена
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                return builder.create();
            default:
                return null;
        }
    }
}
