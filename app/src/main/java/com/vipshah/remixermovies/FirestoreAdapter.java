package com.vipshah.remixermovies;

import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot> {

    protected List<DocumentSnapshot> mDocumentSnapshots = new ArrayList<>();
    private Query mQuery;
    private ListenerRegistration mListenerRegistration;

    protected FirestoreAdapter(Query query) {
        mQuery = query;
    }

    public void stopListeningForLiveEvents() {
        if (mListenerRegistration != null) {
            mListenerRegistration.remove();
            mListenerRegistration = null;
        }

        mDocumentSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery(Query query) {
        stopListeningForLiveEvents();
        mQuery = query;
        startListeningForLiveEvents();
    }

    public void startListeningForLiveEvents() {
        mListenerRegistration = mQuery.addSnapshotListener(this);
    }

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }

        onEventTriggered();
    }

    public void onEventTriggered() {

    }

    private void onDocumentAdded(DocumentChange change) {
        mDocumentSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    private void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            mDocumentSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
            mDocumentSnapshots.remove(change.getOldIndex());
            mDocumentSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    private void onDocumentRemoved(DocumentChange change) {
        mDocumentSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }
}
