package com.example.proyectofinalmovil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.InviteRes
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamInvitesViewModel: ViewModel() {
    private var _teamList = MutableLiveData<MutableList<Team>>()
    val teamList: LiveData<MutableList<Team>> = _teamList

    fun searchInvites(inviteList: InviteRes){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Team>()

            for(invite in inviteList.invites){
                var teamResult = ApiClient.apiClient.getTeamsById(invite.teamId)
                list.add(teamResult)
            }

            withContext(Dispatchers.Main) {
                _teamList.value = list
            }
        }
    }
}