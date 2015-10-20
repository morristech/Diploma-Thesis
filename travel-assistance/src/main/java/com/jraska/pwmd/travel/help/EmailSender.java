package com.jraska.pwmd.travel.help;

import android.content.Context;
import android.content.Intent;
import com.jraska.common.ArgumentCheck;

import javax.inject.Inject;

public class EmailSender {
  //region Fields

  private final Context _context;

  //endregion

  //region Constructors

  public EmailSender(Context context) {
    ArgumentCheck.notNull(context, "context");

    _context = context;
  }

  //endregion

  //region Methods

  public void sendEmail(String[] emails, String subject, String message) {
    Intent emailIntent = new Intent(Intent.ACTION_SEND);

    //array of strings is required for emails
    emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);

    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(Intent.EXTRA_TEXT, message);
    emailIntent.setType("plain/text");
    _context.startActivity(emailIntent);
  }

  public void sendEmail(String email, String subject, String message) {
    String[] emails = {email};
    sendEmail(emails, subject, message);
  }

  //endregion
}
