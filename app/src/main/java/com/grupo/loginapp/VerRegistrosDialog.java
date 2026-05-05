package com.grupo.loginapp;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class VerRegistrosDialog extends DialogFragment {

    private static final String ARG_CONTENIDO = "contenido_registros";
    private String contenidoRegistros;

    public static VerRegistrosDialog newInstance(String contenido) {
        VerRegistrosDialog dialog = new VerRegistrosDialog();
        Bundle args = new Bundle();
        args.putString(ARG_CONTENIDO, contenido);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contenidoRegistros = getArguments().getString(ARG_CONTENIDO);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflar el layout personalizado para el Modal
        View view = getLayoutInflater().inflate(R.layout.dialog_ver_registros, null);

        TextView tvContenido = view.findViewById(R.id.dialog_contenido_registros);
        Button btnCerrar = view.findViewById(R.id.dialog_btn_cerrar);

        // Mostrar el contenido con scroll
        tvContenido.setText(contenidoRegistros);
        tvContenido.setMovementMethod(new ScrollingMovementMethod());

        btnCerrar.setOnClickListener(v -> dismiss());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("REGISTROS ALMACENADOS")
               .setView(view)
               .setCancelable(true);

        return builder.create();
    }
}