package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApplicationImageFileNotFoundException;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.payload.CleanEvent;
import com.repoachiever.service.event.payload.TopologySwapEvent;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/** Represents topology image view. */
@Service
public class TopologyImageView implements IElement<BorderPane> {
  private final UUID id = UUID.randomUUID();

  public TopologyImageView(
          @Autowired PropertiesEntity properties,
          @Autowired ApplicationEventPublisher applicationEventPublisher)
      throws ApplicationImageFileNotFoundException {
    Button button = new Button();

    ElementButtonKt.theme(button, ElementButton.greenButton);
    button.getStylesheets().add(CssResources.globalCssFile);
    button.getStylesheets().add(CssResources.buttonCssFile);
    button.getStylesheets().add(CssResources.textFieldCssFile);

    button.setOnMouseClicked(
        event -> applicationEventPublisher.publishEvent(new TopologySwapEvent()));

    InputStream imageSource =
        getClass().getClassLoader().getResourceAsStream(properties.getImageTopologyName());
    if (Objects.isNull(imageSource)) {
      throw new ApplicationImageFileNotFoundException();
    }

    ImageView imageView = new ImageView(new Image(imageSource));
    imageView.setFitHeight(properties.getImageBarHeight());
    imageView.setFitWidth(properties.getImageBarWidth());

    button.setGraphic(imageView);

    button.setAlignment(Pos.CENTER_RIGHT);

    SplitPane splitPane = new SplitPane(button);
    splitPane.setTooltip(new Tooltip(properties.getButtonTopologyDescription()));

    BorderPane borderPane = new BorderPane();
    borderPane.setRight(splitPane);

    ElementStorage.setElement(id, borderPane);
  }

  /**
   * @see IElementActualizable
   */
  @Override
  public BorderPane getContent() {
    return ElementStorage.getElement(id);
  }
}
