package com.mobdeve.s12.group10.mco

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.SpSmallLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SPAdapter(private var studyPacts: ArrayList<StudyPact>, private val context: Context): RecyclerView.Adapter<SPViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SPViewHolder {
        val spViewBinding: SpSmallLayoutBinding = SpSmallLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SPViewHolder(spViewBinding)
    }

    override fun getItemCount(): Int {
        return studyPacts.size
    }

    override fun onBindViewHolder(holder: SPViewHolder, position: Int) {
        holder.bindData(studyPacts.get(position))

        if (studyPacts.get(position).status != "Upcoming")
            holder.viewBinding.btnJoin.visibility = View.GONE
        else if (getLoggedInUserEmail() in studyPacts.get(position).joiningUsers){
            holder.viewBinding.btnJoin.text = "Joined"
            holder.viewBinding.btnJoin.setTextColor(ContextCompat.getColor(context, R.color.swampyGreen))
            holder.viewBinding.btnJoin.setBackgroundResource(R.drawable.button4)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, SPDetailed::class.java)

            intent.putExtra("SP_ID", studyPacts.get(position)._id)
            intent.putExtra("SP_TITLE", studyPacts.get(position).name)
            intent.putExtra("SP_DATETIME", studyPacts.get(position).dateTime)
            intent.putExtra("SP_LATITUDE", studyPacts.get(position).location.latitude)
            intent.putExtra("SP_LONGITUDE", studyPacts.get(position).location.longitude)
            intent.putExtra("SP_DESCRIPTION", studyPacts.get(position).description)
            intent.putExtra("SP_USERS", studyPacts.get(position).joiningUsers)
            intent.putExtra("SP_STATUS", studyPacts.get(position).status)

            holder.itemView.context.startActivity(intent)
        }

        holder.viewBinding.btnJoin.setOnClickListener {
            FirebaseApp.initializeApp(context)
            val db = FirebaseFirestore.getInstance()

            db.collection("studyPacts").document(studyPacts.get(position)._id).get().addOnSuccessListener { result ->
                val joinedUsers = ArrayList(result.get("joiningUsers") as? List<String>)
                val wasJoined : Boolean
                if (getLoggedInUserEmail() in joinedUsers){
                    wasJoined = true
                    joinedUsers.remove(getLoggedInUserEmail())
                    holder.viewBinding.btnJoin.text = "Join"
                    holder.viewBinding.btnJoin.setTextColor(ContextCompat.getColor(context, R.color.babyGreen))
                    holder.viewBinding.btnJoin.setBackgroundResource(R.drawable.button_gradient2)
                } else {
                    wasJoined = false
                    joinedUsers.add(getLoggedInUserEmail())
                    holder.viewBinding.btnJoin.text = "Joined"
                    holder.viewBinding.btnJoin.setTextColor(ContextCompat.getColor(context, R.color.swampyGreen))
                    holder.viewBinding.btnJoin.setBackgroundResource(R.drawable.button4)
                }

                val sp = hashMapOf(
                    "joiningUsers" to joinedUsers
                )

                db.collection("studyPacts").document(studyPacts.get(position)._id).update(sp as Map<String, Any>).addOnSuccessListener {
                    if (wasJoined)
                        Toast.makeText(context, "Left \"${studyPacts.get(position).name}\"", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "Joined \"${studyPacts.get(position).name}\" ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun filterList(filteredList: ArrayList<StudyPact>) {
        studyPacts = filteredList
        notifyDataSetChanged()
    }

    fun getLoggedInUserEmail(): String? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }
}