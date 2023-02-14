package com.baka3k.stream.player.player.view;

public interface IViewControl {

  /**
   * This function must be call after IPlayer#prepareDataSource()
   *
   * @throws exception if call before  IPlayer#prepareDataSource()
   */
  public void setFitWidth();

  /**
   * This function must be call after IPlayer#prepareDataSource()
   *
   * @throws exception if call before  IPlayer#prepareDataSource()
   */
  public void setFitHeight();
}
