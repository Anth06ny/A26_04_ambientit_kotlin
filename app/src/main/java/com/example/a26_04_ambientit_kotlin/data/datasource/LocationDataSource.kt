package com.example.a26_04_ambientit_kotlin.data.datasource

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task

object LocationDataSource {

    //Récupère la dernière localisation
    //Asynchrone : Déclenche la puce GPS si la dernière localisation est vieille
    fun getLastKnownLocationEconomyMode(context: Context): Task<Location>? {
        //si on n'a pas la permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        //Au lieu d'un check on peut repropager le check de permission à la méthode appelante en ajoutant
        //à la méthode l'annotation
        //@RequiresPermission(
        //anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
        //)

        return LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
    }
}